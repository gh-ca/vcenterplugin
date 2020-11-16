// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package com.dmeplugin.vmware.mo;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;
import org.springframework.util.StringUtils;


public class HostDatastoreSystemMO extends BaseMO {

    public HostDatastoreSystemMO(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    public HostDatastoreSystemMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public ManagedObjectReference findDatastore(String name) throws Exception {
        // added Apache CloudStack specific name convention, we will use custom field "cloud.uuid" as datastore name as well
        CustomFieldsManagerMO cfmMo = new CustomFieldsManagerMO(context, context.getServiceContent().getCustomFieldsManager());
        int key = cfmMo.getCustomFieldKey("Datastore", CustomFieldConstants.CLOUD_UUID);
        assert (key != 0);

        List<ObjectContent> ocs = getDatastorePropertiesOnHostDatastoreSystem(new String[] {"name", String.format("value[%d]", key)});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                if (oc.getPropSet().get(0).getVal().equals(name)) {
                    return oc.getObj();
                }

                if (oc.getPropSet().size() > 1) {
                    DynamicProperty prop = oc.getPropSet().get(1);
                    if (prop != null && prop.getVal() != null) {
                        if (prop.getVal() instanceof CustomFieldStringValue) {
                            String val = ((CustomFieldStringValue)prop.getVal()).getValue();
                            if (val.equalsIgnoreCase(name)) {
                                return oc.getObj();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<HostUnresolvedVmfsVolume> queryUnresolvedVmfsVolumes() throws Exception {
        return context.getService().queryUnresolvedVmfsVolumes(mor);
    }

    public List<VmfsDatastoreOption> queryVmfsDatastoreExpandOptions(DatastoreMO datastoreMo) throws Exception {
        return context.getService().queryVmfsDatastoreExpandOptions(mor, datastoreMo.getMor());
    }

    public void expandVmfsDatastore(DatastoreMO datastoreMo, VmfsDatastoreExpandSpec vmfsDatastoreExpandSpec) throws Exception {
        context.getService().expandVmfsDatastore(mor, datastoreMo.getMor(), vmfsDatastoreExpandSpec);
    }

    // storeUrl in nfs://host/exportpath format
    public ManagedObjectReference findDatastoreByUrl(String storeUrl) throws Exception {
        assert (storeUrl != null);

        List<ManagedObjectReference> datastores = getDatastores();
        if (datastores != null && datastores.size() > 0) {
            for (ManagedObjectReference morDatastore : datastores) {
                NasDatastoreInfo info = getNasDatastoreInfo(morDatastore);
                if (info != null) {
                    URI uri = new URI(storeUrl);
                    String vmwareStyleUrl = "netfs://" + uri.getHost() + "/" + uri.getPath() + "/";
                    if (info.getUrl().equals(vmwareStyleUrl)) {
                        return morDatastore;
                    }
                }
            }
        }

        return null;
    }

    public ManagedObjectReference findDatastoreByName(String datastoreName) throws Exception {
        assert (datastoreName != null);

        List<ManagedObjectReference> datastores = getDatastores();

        if (datastores != null) {
            for (ManagedObjectReference morDatastore : datastores) {
                DatastoreInfo info = getDatastoreInfo(morDatastore);

                if (info != null) {
                    if (info.getName().equals(datastoreName)) {
                        return morDatastore;
                    }
                }
            }
        }

        return null;
    }

    // TODO this is a hacking helper method, when we can pass down storage pool info along with volume
    // we should be able to find the datastore by name
    public ManagedObjectReference findDatastoreByExportPath(String exportPath) throws Exception {
        assert (exportPath != null);

        List<ManagedObjectReference> datastores = getDatastores();
        if (datastores != null && datastores.size() > 0) {
            for (ManagedObjectReference morDatastore : datastores) {
                DatastoreMO dsMo = new DatastoreMO(context, morDatastore);
                if (dsMo.getInventoryPath().equals(exportPath)) {
                    return morDatastore;
                }

                NasDatastoreInfo info = getNasDatastoreInfo(morDatastore);
                if (info != null) {
                    String vmwareUrl = info.getUrl();
                    if (vmwareUrl.charAt(vmwareUrl.length() - 1) == '/') {
                        vmwareUrl = vmwareUrl.substring(0, vmwareUrl.length() - 1);
                    }

                    URI uri = new URI(vmwareUrl);
                    if (uri.getPath().equals("/" + exportPath)) {
                        return morDatastore;
                    }
                }
            }
        }

        return null;
    }

    public List<HostScsiDisk> queryAvailableDisksForVmfs() throws Exception {
        return context.getService().queryAvailableDisksForVmfs(mor, null);
    }

    public ManagedObjectReference createVmfsDatastore(String datastoreName,
                                                      HostScsiDisk hostScsiDisk,
                                                      int vmfsMajorVersion,
                                                      int blockSize,
                                                      long totalSectors,
                                                      int unmapGranularity,
                                                      String unmapPriority) throws Exception {
        // just grab the first instance of VmfsDatastoreOption
        VmfsDatastoreOption vmfsDatastoreOption = context.getService().queryVmfsDatastoreCreateOptions(mor, hostScsiDisk.getDevicePath(), vmfsMajorVersion).get(0);

        VmfsDatastoreCreateSpec vmfsDatastoreCreateSpec = (VmfsDatastoreCreateSpec)vmfsDatastoreOption.getSpec();

        // set the name of the datastore to be created
        vmfsDatastoreCreateSpec.getVmfs().setVolumeName(datastoreName);
        vmfsDatastoreCreateSpec.getVmfs().setBlockSize(blockSize);
        vmfsDatastoreCreateSpec.getVmfs().setUnmapGranularity(unmapGranularity);
        vmfsDatastoreCreateSpec.getVmfs().setUnmapPriority(unmapPriority);
        vmfsDatastoreCreateSpec.getPartition().setTotalSectors(totalSectors);

        return context.getService().createVmfsDatastore(mor, vmfsDatastoreCreateSpec);
    }

    public boolean deleteDatastore(String name) throws Exception {
        ManagedObjectReference morDatastore = findDatastore(name);
        if (morDatastore != null) {
            context.getService().removeDatastore(mor, morDatastore);
            return true;
        }
        return false;
    }

    public ManagedObjectReference createNfsDatastore(String host, int port, String exportPath, String uuid, String accessMode,String type,String securityType) throws Exception {

        HostNasVolumeSpec spec = new HostNasVolumeSpec();
        spec.setRemoteHost(host);
        spec.setRemotePath(exportPath);
        //NFS41  NFS 默认NFS
        if (StringUtils.isEmpty(type)) {
            spec.setType("NFS");
        }
        /**
         * 版本 4.1 需要设置安全类型，默认AUTH_SYS
         * AUTH_SYS	 无域环境
         * SEC_KRB5
         * SEC_KRB5I
         */
        if (!StringUtils.isEmpty(securityType)) {
            spec.setSecurityType(securityType);
        }
        spec.setType(type);
        //需要设置datastore名称
        spec.setLocalPath(uuid);
        // readOnly/readWrite
        if (!StringUtils.isEmpty(accessMode)) {
            spec.setAccessMode(accessMode);
        } else {
            spec.setAccessMode("readWrite");
        }
        return context.getService().createNasDatastore(mor, spec);
    }

    public List<ManagedObjectReference> getDatastores() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "datastore");
    }

    public DatastoreInfo getDatastoreInfo(ManagedObjectReference morDatastore) throws Exception {
        return (DatastoreInfo) context.getVimClient().getDynamicProperty(morDatastore, "info");
    }

    public NasDatastoreInfo getNasDatastoreInfo(ManagedObjectReference morDatastore) throws Exception {
        DatastoreInfo info = (DatastoreInfo) context.getVimClient().getDynamicProperty(morDatastore, "info");
        if (info instanceof NasDatastoreInfo) {
            return (NasDatastoreInfo)info;
        }
        return null;
    }

    public HostResignatureRescanResult resignatureUnresolvedVmfsVolume(HostUnresolvedVmfsResignatureSpec resolutionSpec) throws Exception {
        ManagedObjectReference task = context.getService().resignatureUnresolvedVmfsVolumeTask(mor, resolutionSpec);

        boolean result = context.getVimClient().waitForTask(task);

        if (result) {
            context.waitForTaskProgressDone(task);

            TaskMO taskMo = new TaskMO(context, task);

            return (HostResignatureRescanResult)taskMo.getTaskInfo().getResult();
        } else {
            throw new Exception("Unable to register vm due to " + TaskMO.getTaskFailureInfo(context, task));
        }
    }

    public List<ObjectContent> getDatastorePropertiesOnHostDatastoreSystem(String[] propertyPaths) throws Exception {

        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("Datastore");
        pSpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec hostDsSys2DatastoreTraversal = new TraversalSpec();
        hostDsSys2DatastoreTraversal.setType("HostDatastoreSystem");
        hostDsSys2DatastoreTraversal.setPath("datastore");
        hostDsSys2DatastoreTraversal.setName("hostDsSys2DatastoreTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(hostDsSys2DatastoreTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        return context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);
    }
    public void unmapVmfsVolumeExTask(List<String> vmfsUuids) throws Exception {
        context.getService().unmapVmfsVolumeExTask(mor, vmfsUuids);
    }
}

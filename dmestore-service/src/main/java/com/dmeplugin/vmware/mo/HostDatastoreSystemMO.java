package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostDatastoreSystemMO extends BaseMO {
    public HostDatastoreSystemMO(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    public ManagedObjectReference findDatastore(String name) throws Exception {
        ServiceContent serviceContent = context.getServiceContent();
        ManagedObjectReference customFieldsManager = serviceContent.getCustomFieldsManager();
        CustomFieldsManagerMO cfmMo = new CustomFieldsManagerMO(context, customFieldsManager);
        int key = cfmMo.getCustomFieldKey("Datastore", CustomFieldConstants.CLOUD_UUID);
        assert key != 0;

        List<ObjectContent> ocs = getDatastorePropertiesOnHostDatastoreSystem(
            new String[] {"name", String.format("value[%d]", key)});
        if (ocs == null) {
            return null;
        }
        for (ObjectContent oc : ocs) {
            if (oc.getPropSet().get(0).getVal().equals(name)) {
                return oc.getObj();
            }

            if (oc.getPropSet().size() > 1) {
                DynamicProperty prop = oc.getPropSet().get(1);
                if (prop != null && prop.getVal() != null) {
                    if (prop.getVal() instanceof CustomFieldStringValue) {
                        String val = ((CustomFieldStringValue) prop.getVal()).getValue();
                        if (val.equalsIgnoreCase(name)) {
                            return oc.getObj();
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<VmfsDatastoreOption> queryVmfsDatastoreExpandOptions(DatastoreMO datastoreMo) throws Exception {
        return context.getService().queryVmfsDatastoreExpandOptions(mor, datastoreMo.getMor());
    }

    public void expandVmfsDatastore(DatastoreMO datastoreMo, VmfsDatastoreExpandSpec vmfsDatastoreExpandSpec)
        throws Exception {
        context.getService().expandVmfsDatastore(mor, datastoreMo.getMor(), vmfsDatastoreExpandSpec);
    }

    public List<HostScsiDisk> queryAvailableDisksForVmfs() throws Exception {
        return context.getService().queryAvailableDisksForVmfs(mor, null);
    }

    public ManagedObjectReference createVmfsDatastore(String datastoreName, HostScsiDisk hostScsiDisk,
        int vmfsMajorVersion, int blockSize, long totalSectors, int unmapGranularity, String unmapPriority)
        throws Exception {
        VmfsDatastoreOption vmfsDatastoreOption = context.getService()
            .queryVmfsDatastoreCreateOptions(mor, hostScsiDisk.getDevicePath(), vmfsMajorVersion)
            .get(0);

        VmfsDatastoreCreateSpec vmfsDatastoreCreateSpec = (VmfsDatastoreCreateSpec) vmfsDatastoreOption.getSpec();

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

    public ManagedObjectReference createNfsDatastore(String host, int port, String exportPath, String uuid,
        String accessMode, String type, String securityType) throws Exception {
        HostNasVolumeSpec spec = new HostNasVolumeSpec();
        spec.setRemoteHost(host);
        spec.setRemotePath(exportPath);
        if (StringUtils.isEmpty(type)) {
            spec.setType("NFS");
        }
        if (!StringUtils.isEmpty(securityType)) {
            spec.setSecurityType(securityType);
        }
        spec.setType(type);
        // 需要设置datastore名称
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
        DatastoreInfo info = context.getVimClient().getDynamicProperty(morDatastore, "info");
        if (info instanceof NasDatastoreInfo) {
            return (NasDatastoreInfo) info;
        }
        return null;
    }

    public List<ObjectContent> getDatastorePropertiesOnHostDatastoreSystem(String[] propertyPaths) throws Exception {
        PropertySpec propertySpec = new PropertySpec();
        propertySpec.setType("Datastore");
        propertySpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec hostDsSys2DatastoreTraversal = new TraversalSpec();
        hostDsSys2DatastoreTraversal.setType("HostDatastoreSystem");
        hostDsSys2DatastoreTraversal.setPath("datastore");
        hostDsSys2DatastoreTraversal.setName("hostDsSys2DatastoreTraversal");

        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(mor);
        objectSpec.setSkip(Boolean.TRUE);
        objectSpec.getSelectSet().add(hostDsSys2DatastoreTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(propertySpec);
        pfSpec.getObjectSet().add(objectSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        return context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);
    }

    public void unmapVmfsVolumeExTask(List<String> vmfsUuids) throws Exception {
        context.getService().unmapVmfsVolumeExTask(mor, vmfsUuids);
    }
}

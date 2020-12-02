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

import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatastoreMO extends BaseMO {
    private static final Logger s_logger = LoggerFactory.getLogger(DatastoreMO.class);

    private String name;

    private Pair<DatacenterMO, String> ownerDc;

    public DatastoreMO(VmwareContext context, ManagedObjectReference morDatastore) {
        super(context, morDatastore);
    }

    public DatastoreMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    @Override
    public String getName() throws Exception {
        if (name == null) {
            name = context.getVimClient().getDynamicProperty(mor, "name");
        }

        return name;
    }

    public DatastoreMO(VmwareContext context, String dsName) throws Exception {
        super(context, null);

        mor = this.context.getVimClient().getDecendentMoRef(this.context.getRootFolder(), "Datastore", dsName);
        if (mor == null) {
            s_logger.error("Unable to locate ds " + dsName);
        }
    }

    public DatastoreInfo getInfo() throws Exception {
        return (DatastoreInfo) context.getVimClient().getDynamicProperty(mor, "info");
    }

    public VmfsDatastoreInfo getVmfsDatastoreInfo() throws Exception {
        return (VmfsDatastoreInfo) context.getVimClient().getDynamicProperty(mor, "info");
    }

    public DatastoreSummary getSummary() throws Exception {
        return (DatastoreSummary) context.getVimClient().getDynamicProperty(mor, "summary");
    }

    public List<ManagedObjectReference> getVm() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "vm");
    }

    public List<DatastoreHostMount> getHostMounts() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "host");
    }

    public Pair<DatacenterMO, String> getOwnerDatacenter() throws Exception {
        if (ownerDc != null) {
            return ownerDc;
        }

        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("Datacenter");
        pSpec.getPathSet().add("name");

        TraversalSpec folderParentTraversal = new TraversalSpec();
        folderParentTraversal.setType("Folder");
        folderParentTraversal.setPath("parent");
        folderParentTraversal.setName("folderParentTraversal");
        SelectionSpec sSpec = new SelectionSpec();
        sSpec.setName("folderParentTraversal");
        folderParentTraversal.getSelectSet().add(sSpec);

        TraversalSpec dsParentTraversal = new TraversalSpec();
        dsParentTraversal.setType("Datastore");
        dsParentTraversal.setPath("parent");
        dsParentTraversal.setName("dsParentTraversal");
        dsParentTraversal.getSelectSet().add(folderParentTraversal);

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(getMor());
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(dsParentTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> ocs = context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        assert (ocs != null && ocs.size() > 0);
        assert (ocs.get(0).getObj() != null);
        assert (ocs.get(0).getPropSet() != null);
        String dcName = ocs.get(0).getPropSet().get(0).getVal().toString();
        ownerDc = new Pair<>(new DatacenterMO(context, ocs.get(0).getObj()), dcName);
        return ownerDc;
    }

    public void renameDatastore(String newDatastoreName) throws Exception {
        context.getService().renameDatastore(mor, newDatastoreName);
    }

    public boolean moveDatastoreFile(String srcFilePath, ManagedObjectReference morSrcDc,
        ManagedObjectReference morDestDs, String destFilePath, ManagedObjectReference morDestDc, boolean forceOverwrite)
        throws Exception {

        String srcDsName = getName();
        DatastoreMO destDsMo = new DatastoreMO(context, morDestDs);
        String destDsName = destDsMo.getName();

        ManagedObjectReference morFileManager = context.getServiceContent().getFileManager();
        String srcFullPath = srcFilePath;
        if (!DatastoreFile.isFullDatastorePath(srcFullPath)) {
            srcFullPath = String.format("[%s] %s", srcDsName, srcFilePath);
        }

        String destFullPath = destFilePath;
        if (!DatastoreFile.isFullDatastorePath(destFullPath)) {
            destFullPath = String.format("[%s] %s", destDsName, destFilePath);
        }

        ManagedObjectReference morTask = context.getService()
            .moveDatastoreFileTask(morFileManager, srcFullPath, morSrcDc, destFullPath, morDestDc, forceOverwrite);

        boolean result = context.getVimClient().waitForTask(morTask);
        if (result) {
            context.waitForTaskProgressDone(morTask);
            return true;
        } else {
            s_logger.error(
                "VMware moveDatgastoreFile_Task failed due to " + TaskMO.getTaskFailureInfo(context, morTask));
        }
        return false;
    }

    public void refreshDatastore() throws Exception {
        context.getService().refreshDatastore(mor);
    }
}

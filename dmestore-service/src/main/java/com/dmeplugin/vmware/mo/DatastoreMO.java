package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VmfsDatastoreInfo;

import java.util.ArrayList;
import java.util.List;

public class DatastoreMO extends BaseMO {
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
    }

    public DatastoreInfo getInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "info");
    }

    public VmfsDatastoreInfo getVmfsDatastoreInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "info");
    }

    public DatastoreSummary getSummary() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "summary");
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

        PropertySpec propertySpec = new PropertySpec();
        propertySpec.setType("Datacenter");
        propertySpec.getPathSet().add("name");

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

        ObjectSpec objectSpec = new ObjectSpec();
        objectSpec.setObj(getMor());
        objectSpec.setSkip(true);
        objectSpec.getSelectSet().add(dsParentTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(propertySpec);
        pfSpec.getObjectSet().add(objectSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> ocs = context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        assert ocs != null && ocs.size() > 0;
        assert ocs.get(0).getObj() != null;
        assert ocs.get(0).getPropSet() != null;
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
        }
        return false;
    }

    public String[] listDirContent(String path) throws Exception {
        String fullPath = path;
        if (!DatastoreFile.isFullDatastorePath(fullPath)) {
            fullPath = String.format("[%s] %s", getName(), fullPath);
        }

        Pair<DatacenterMO, String> dcPair = getOwnerDatacenter();
        String dcName = dcPair.second();
        String url = context.composeDatastoreBrowseUrl(dcName, fullPath);

        return context.listDatastoreDirContent(url);
    }

    public void refreshDatastore() throws Exception {
        context.getService().refreshDatastore(mor);
    }
}

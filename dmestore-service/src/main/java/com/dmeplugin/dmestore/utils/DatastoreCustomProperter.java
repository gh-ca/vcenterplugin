package com.dmeplugin.dmestore.utils;

import com.dmeplugin.dmestore.services.DmeNFSAccessServiceImpl;
import com.dmeplugin.dmestore.services.DmeStorageServiceImpl;
import com.dmeplugin.dmestore.services.VmfsAccessServiceImpl;
import com.dmeplugin.vmware.VCConnectionHelper;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.data.query.*;
import com.vmware.vise.vim.data.VimObjectReferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

public class DatastoreCustomProperter implements PropertyProviderAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(DatastoreCustomProperter.class);

    @Autowired
    private VmfsAccessServiceImpl vmfsAccessService;

    @Autowired
    private DmeNFSAccessServiceImpl dmeNfsAccessService;

    @Autowired
    private DmeStorageServiceImpl dmeStorageService;

    @Autowired
    @Lazy
    private VimObjectReferenceService vimObjectReferenceService;

    @Autowired
    private VCConnectionHelper vcConnectionHelper;

    public VimObjectReferenceService getVimObjectReferenceService() {
        return vimObjectReferenceService;
    }

    public void setVimObjectReferenceService(VimObjectReferenceService vimObjectReferenceService) {
        this.vimObjectReferenceService = vimObjectReferenceService;
    }

    public DatastoreCustomProperter(DataServiceExtensionRegistry extensionRegistry) {
        TypeInfo vmTypeInfo = new TypeInfo();
        vmTypeInfo.type = "Datastore";
        vmTypeInfo.properties = new String[] { "isVmfs","isNfs","hasVm" };
        TypeInfo[] providerTypes = new TypeInfo[] {vmTypeInfo};

        extensionRegistry.registerDataAdapter(this, providerTypes);
    }

    public DatastoreCustomProperter(){

    }

    @Override
    public ResultSet getProperties(PropertyRequestSpec propertyRequest) {

        ResultSet resultSet = new ResultSet();

        try {
            List<ResultItem> resultItems = new ArrayList<ResultItem>();

            for (Object objRef : propertyRequest.objects) {
                ResultItem resultItem =new ResultItem();
                PropertyValue isVmfsDataStore = new PropertyValue();

                isVmfsDataStore.resourceObject = objRef;

                isVmfsDataStore.propertyName = "isVmfs";

                PropertyValue isNfsDataStore = new PropertyValue();

                isNfsDataStore.resourceObject = objRef;

                isNfsDataStore.propertyName = "isNfs";

                PropertyValue hasVmDatastore = new PropertyValue();

                hasVmDatastore.resourceObject = objRef;

                hasVmDatastore.propertyName = "hasVm";

                String entityType = vimObjectReferenceService.getResourceObjectType(objRef);
                String entityName = vimObjectReferenceService.getValue(objRef);
                String serverGuid = vimObjectReferenceService.getServerGuid(objRef);
                ManagedObjectReference mor=new ManagedObjectReference();
                mor.setType(entityType);
                mor.setValue(entityName);
                String objectid=vcConnectionHelper.mor2ObjectId(mor,serverGuid);

                boolean isVmfs=vmfsAccessService.isVmfs(objectid);
                isVmfsDataStore.value = isVmfs;

                boolean isNfs= dmeNfsAccessService.isNfs(objectid);
                isNfsDataStore.value= isNfs;

                boolean hasVm=dmeStorageService.hasVmOnDatastore(objectid);
                hasVmDatastore.value=hasVm;

                resultItem.resourceObject=objRef;
                resultItem.properties = new PropertyValue[] {isVmfsDataStore,isNfsDataStore,hasVmDatastore };
                resultItems.add(resultItem);
            }


            resultSet.items = resultItems.toArray(new ResultItem[] {});

        } catch (Exception e) {
            LOG.error("vmfsnfsLogic.getProperties error: " + e);
        }

        return resultSet;
    }
}

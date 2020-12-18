package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HostNasVolumeSpec;
import com.vmware.vim25.HostScsiDisk;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NasDatastoreInfo;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VmfsDatastoreCreateSpec;
import com.vmware.vim25.VmfsDatastoreExpandSpec;
import com.vmware.vim25.VmfsDatastoreOption;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * HostDatastoreSystemMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class HostDatastoreSystemMO extends BaseMO {
    /**
     * HostDatastoreSystemMO
     *
     * @param context          context
     * @param morHostDatastore morHostDatastore
     */
    public HostDatastoreSystemMO(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    /**
     * findDatastore
     *
     * @param name name
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
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

    /**
     * queryVmfsDatastoreExpandOptions
     *
     * @param datastoreMo datastoreMo
     * @return List
     * @throws Exception Exception
     */
    public List<VmfsDatastoreOption> queryVmfsDatastoreExpandOptions(DatastoreMO datastoreMo) throws Exception {
        return context.getService().queryVmfsDatastoreExpandOptions(mor, datastoreMo.getMor());
    }

    /**
     * expandVmfsDatastore
     *
     * @param datastoreMo             datastoreMo
     * @param vmfsDatastoreExpandSpec vmfsDatastoreExpandSpec
     * @throws Exception Exception
     */
    public void expandVmfsDatastore(DatastoreMO datastoreMo, VmfsDatastoreExpandSpec vmfsDatastoreExpandSpec)
        throws Exception {
        context.getService().expandVmfsDatastore(mor, datastoreMo.getMor(), vmfsDatastoreExpandSpec);
    }

    /**
     * queryAvailableDisksForVmfs
     *
     * @return List
     * @throws Exception Exception
     */
    public List<HostScsiDisk> queryAvailableDisksForVmfs() throws Exception {
        return context.getService().queryAvailableDisksForVmfs(mor, null);
    }

    /**
     * createVmfsDatastore
     *
     * @param datastoreName    datastoreName
     * @param hostScsiDisk     hostScsiDisk
     * @param vmfsMajorVersion vmfsMajorVersion
     * @param blockSize        blockSize
     * @param totalSectors     totalSectors
     * @param unmapGranularity unmapGranularity
     * @param unmapPriority    unmapPriority
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference createVmfsDatastore(String datastoreName, HostScsiDisk hostScsiDisk,
                                                      int vmfsMajorVersion, int blockSize, long totalSectors,
                                                      int unmapGranularity, String unmapPriority)
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

    /**
     * deleteDatastore
     *
     * @param name name
     * @return boolean
     * @throws Exception Exception
     */
    public boolean deleteDatastore(String name) throws Exception {
        ManagedObjectReference morDatastore = findDatastore(name);
        if (morDatastore != null) {
            context.getService().removeDatastore(mor, morDatastore);
            return true;
        }
        return false;
    }

    /**
     * createNfsDatastore
     *
     * @param host         host
     * @param port         port
     * @param exportPath   exportPath
     * @param uuid         uuid
     * @param accessMode   accessMode
     * @param type         type
     * @param securityType securityType
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference createNfsDatastore(String host, int port, String exportPath, String uuid,
                                                     String accessMode, String type, String securityType)
        throws Exception {
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

    /**
     * getDatastores
     *
     * @return List
     * @throws Exception Exception
     */
    public List<ManagedObjectReference> getDatastores() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "datastore");
    }

    /**
     * getDatastoreInfo
     *
     * @param morDatastore morDatastore
     * @return DatastoreInfo
     * @throws Exception Exception
     */
    public DatastoreInfo getDatastoreInfo(ManagedObjectReference morDatastore) throws Exception {
        return (DatastoreInfo) context.getVimClient().getDynamicProperty(morDatastore, "info");
    }

    /**
     * getNasDatastoreInfo
     *
     * @param morDatastore morDatastore
     * @return NasDatastoreInfo
     * @throws Exception Exception
     */
    public NasDatastoreInfo getNasDatastoreInfo(ManagedObjectReference morDatastore) throws Exception {
        DatastoreInfo info = context.getVimClient().getDynamicProperty(morDatastore, "info");
        if (info instanceof NasDatastoreInfo) {
            return (NasDatastoreInfo) info;
        }
        return null;
    }

    /**
     * getDatastorePropertiesOnHostDatastoreSystem
     *
     * @param propertyPaths propertyPaths
     * @return List
     * @throws Exception Exception
     */
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

    /**
     * unmapVmfsVolumeExTask
     *
     * @param vmfsUuids vmfsUuids
     * @throws Exception Exception
     */
    public void unmapVmfsVolumeExTask(List<String> vmfsUuids) throws Exception {
        context.getService().unmapVmfsVolumeExTask(mor, vmfsUuids);
    }
}
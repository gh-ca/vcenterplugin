package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;

import com.google.gson.Gson;
import com.vmware.vim25.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class HostDatastoreSystemMo extends BaseMo {
    private static final Logger LOG = LoggerFactory.getLogger(HostDatastoreSystemMo.class);

    /**
     * HostDatastoreSystemMO
     *
     * @param context context
     * @param morHostDatastore morHostDatastore
     */
    public HostDatastoreSystemMo(VmwareContext context, ManagedObjectReference morHostDatastore) {
        super(context, morHostDatastore);
    }

    /**
     * queryVmfsDatastoreExpandOptions
     *
     * @param datastoreMo datastoreMo
     * @return List
     * @throws Exception Exception
     */
    public List<VmfsDatastoreOption> queryVmfsDatastoreExpandOptions(DatastoreMo datastoreMo) throws Exception {
        return context.getService().queryVmfsDatastoreExpandOptions(mor, datastoreMo.getMor());
    }

    /**
     * expandVmfsDatastore
     *
     * @param datastoreMo datastoreMo
     * @param vmfsDatastoreExpandSpec vmfsDatastoreExpandSpec
     * @throws Exception Exception
     */
    public void expandVmfsDatastore(DatastoreMo datastoreMo, VmfsDatastoreExpandSpec vmfsDatastoreExpandSpec)
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
     * @param datastoreName datastoreName
     * @param hostScsiDisk hostScsiDisk
     * @param vmfsMajorVersion vmfsMajorVersion
     * @param blockSize blockSize
     * @param totalSectors totalSectors
     * @param unmapGranularity unmapGranularity
     * @param unmapPriority unmapPriority
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference createVmfsDatastore(String datastoreName, HostScsiDisk hostScsiDisk,
        int vmfsMajorVersion, int blockSize, long totalSectors, int unmapGranularity, String unmapPriority)
        throws Exception {
        LOG.info("creating vmfs on vcenter!");
        VmfsDatastoreOption vmfsDatastoreOption = context.getService()
            .queryVmfsDatastoreCreateOptions(mor, hostScsiDisk.getDevicePath(), vmfsMajorVersion)
            .get(0);

        VmfsDatastoreCreateSpec vmfsDatastoreCreateSpec = (VmfsDatastoreCreateSpec) vmfsDatastoreOption.getSpec();

        vmfsDatastoreCreateSpec.getVmfs().setVolumeName(datastoreName);
        vmfsDatastoreCreateSpec.getVmfs().setBlockSize(blockSize);
        if (unmapGranularity != 0) {
            vmfsDatastoreCreateSpec.getVmfs().setUnmapGranularity(unmapGranularity);
        }
        vmfsDatastoreCreateSpec.getVmfs().setUnmapPriority(unmapPriority);
        vmfsDatastoreCreateSpec.getPartition().setTotalSectors(totalSectors);

        LOG.info("==vmware createVmfsDatastore params==datastoreName{},request params={}", datastoreName,
            new Gson().toJson(vmfsDatastoreCreateSpec));

        return context.getService().createVmfsDatastore(mor, vmfsDatastoreCreateSpec);
    }

    /**
     * deleteDatastore
     *
     * @param datastoreMor datastoreMor
     * @return boolean
     * @throws Exception Exception
     */
    public boolean deleteDatastore(ManagedObjectReference datastoreMor) throws Exception {
        if (datastoreMor != null) {
            context.getService().removeDatastore(mor, datastoreMor);
            return true;
        }
        return false;
    }

    public ManagedObjectReference findDatastore(String name) throws Exception {
        // added Apache CloudStack specific name convention, we will use custom field "cloud.uuid" as datastore name as well
        CustomFieldsManagerMo cfmMo = new CustomFieldsManagerMo(context, context.getServiceContent().getCustomFieldsManager());
        int key = cfmMo.getCustomFieldKey("Datastore", CustomFieldConstants.CLOUD_UUID);
        assert (key != 0);

        List<ObjectContent> ocs = getDatastorePropertiesOnHostDatastoreSystem(new String[] {"name", String.format("value[%d]", key)});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                if (oc.getPropSet().get(0).getVal().equals(name))
                    return oc.getObj();

                if (oc.getPropSet().size() > 1) {
                    DynamicProperty prop = oc.getPropSet().get(1);
                    if (prop != null && prop.getVal() != null) {
                        if (prop.getVal() instanceof CustomFieldStringValue) {
                            String val = ((CustomFieldStringValue)prop.getVal()).getValue();
                            if (val.equalsIgnoreCase(name))
                                return oc.getObj();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * createNfsDatastore
     *
     * @param host host
     * @param port port
     * @param exportPath exportPath
     * @param uuid uuid
     * @param accessMode accessMode
     * @param type type
     * @param securityType securityType
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference createNfsDatastore(String host, int port, String exportPath, String uuid,
        String accessMode, String type, String securityType) throws Exception {
        HostNasVolumeSpec spec = new HostNasVolumeSpec();
        spec.setRemoteHost(host);
        spec.getRemoteHostNames().add(host);
        spec.setRemotePath(exportPath);
        if (!StringUtils.isEmpty(type)) {
            spec.setType(type);
        }
        if (!StringUtils.isEmpty(securityType)) {
            if (HostNasVolumeSecurityType.AUTH_SYS.value().equalsIgnoreCase(securityType)) {
                spec.setSecurityType(HostNasVolumeSecurityType.AUTH_SYS.value());
            } else if (HostNasVolumeSecurityType.SEC_KRB_5.value().equalsIgnoreCase(securityType)) {
                spec.setSecurityType(HostNasVolumeSecurityType.SEC_KRB_5.value());
            } else {
                spec.setSecurityType(HostNasVolumeSecurityType.SEC_KRB_5_I.value());
            }
        }

        // 需要设置datastore名称
        spec.setLocalPath(uuid);

        // readOnly/readWrite
        if (!StringUtils.isEmpty(accessMode)) {
            if ("readOnly".equalsIgnoreCase(accessMode)) {
                spec.setAccessMode("readOnly");
            } else {
                spec.setAccessMode("readWrite");
            }
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
}

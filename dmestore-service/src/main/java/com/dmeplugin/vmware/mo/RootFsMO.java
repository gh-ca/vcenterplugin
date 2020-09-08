package com.dmeplugin.vmware.mo;

import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RootFsMO extends BaseMO{
    public RootFsMO(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }

    public RootFsMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    public List<Pair<ManagedObjectReference, String>> getAllDatacenterOnRootFs() throws Exception {
        List<Pair<ManagedObjectReference, String>> datacenters = new ArrayList<Pair<ManagedObjectReference, String>>();

        List<ObjectContent> ocs = getDatacenterPropertiesOnRootfs(new String[] {"name"});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                String dcName = oc.getPropSet().get(0).getVal().toString();
                datacenters.add(new Pair<ManagedObjectReference, String>(oc.getObj(), dcName));
            }
        }

        return datacenters;
    }

    public List<ObjectContent> getDatacenterPropertiesOnRootfs(String[] propertyPaths) throws Exception {

        PropertySpec propertySpec = new PropertySpec();
        propertySpec.setAll(Boolean.FALSE);
        propertySpec.getPathSet().add("name");
        propertySpec.setType("Datacenter");

        //SelectionSpec是TraversalSpec的一个引用。
        SelectionSpec sSpec = new SelectionSpec();
        sSpec.setName("folder2childEntity");

        TraversalSpec traversalSpec = new TraversalSpec();
        //给traversalSpec设置名称
        traversalSpec.setName("folder2childEntity");
        //从rootFolder开始遍历，rootFolder类型是Folder
        traversalSpec.setType("Folder");
        //rootFolder拥有childEntity属性，清单结构图中指向的便是Datacenter
        traversalSpec.setPath("childEntity");
        //false表示不对其本身进行收集，只对其下对象进行收集
        traversalSpec.setSkip(false);
        //将sSpec添加到SelectionSpec集合中
        traversalSpec.getSelectSet().add(sSpec);

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(_mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(traversalSpec);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(propertySpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        return _context.getService().retrieveProperties(_context.getPropertyCollector(), pfSpecArr);

    }
    /**
     * 获取host主机列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllHostOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找hosts
        List<Pair<ManagedObjectReference, String>> hosts = _context.inFolderByType(_context.getRootFolder(),
                "HostSystem");
        return hosts;
    }

    /**
     * 获取集群列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllClusterOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找clusters
        List<Pair<ManagedObjectReference, String>> clusters = _context.inFolderByType(_context.getRootFolder(),
                "ClusterComputeResource");
        return clusters;
    }

    /**
     * 获取datastore列表
     */
    public List<Pair<ManagedObjectReference, String>> getAllDatastoreOnRootFs()
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        //查找datastores
        List<Pair<ManagedObjectReference, String>> hosts = _context.inFolderByType(_context.getRootFolder(),
                "Datastore");
        return hosts;
    }
}

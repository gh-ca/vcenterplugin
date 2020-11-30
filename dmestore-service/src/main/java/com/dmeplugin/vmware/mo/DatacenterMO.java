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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.dmeplugin.vmware.util.Pair;
import com.dmeplugin.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DatacenterConfigInfo;
import com.vmware.vim25.DVPortgroupConfigInfo;
import com.vmware.vim25.DistributedVirtualSwitchPortConnection;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VirtualEthernetCardDistributedVirtualPortBackingInfo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatacenterMO extends BaseMO {
    private static final Logger s_logger = LoggerFactory.getLogger(DatacenterMO.class);

    public DatacenterMO(VmwareContext context, ManagedObjectReference morDc) {
        super(context, morDc);
    }

    public DatacenterMO(VmwareContext context, String dcName) throws Exception {
        super(context, null);

        mor = this.context.getVimClient().getDecendentMoRef(this.context.getRootFolder(), "Datacenter", dcName);
        if (mor == null) {
            s_logger.error("Unable to locate DC " + dcName);
        }
    }

    @Override
    public String getName() throws Exception {
        return (String) context.getVimClient().getDynamicProperty(mor, "name");
    }

    public ManagedObjectReference findDatastore(String name) throws Exception {
        assert (name != null);

        List<ObjectContent> ocs = getDatastorePropertiesOnDatacenter(new String[] {"name"});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                if (oc.getPropSet().get(0).getVal().toString().equals(name)) {
                    return oc.getObj();
                }
            }
        }
        return null;
    }

    public ManagedObjectReference getVmFolder() throws Exception {
        return (ManagedObjectReference) context.getVimClient().getDynamicProperty(mor, "vmFolder");
    }

    public List<ObjectContent> getDatastorePropertiesOnDatacenter(String[] propertyPaths) throws Exception {

        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("Datastore");
        pSpec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec dc2DatastoreTraversal = new TraversalSpec();
        dc2DatastoreTraversal.setType("Datacenter");
        dc2DatastoreTraversal.setPath("datastore");
        dc2DatastoreTraversal.setName("dc2DatastoreTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(dc2DatastoreTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        return context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

    }


    public static Pair<DatacenterMO, String> getOwnerDatacenter(VmwareContext context, ManagedObjectReference morEntity) throws Exception {
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("Datacenter");
        pSpec.getPathSet().add("name");

        TraversalSpec entityParentTraversal = new TraversalSpec();
        entityParentTraversal.setType("ManagedEntity");
        entityParentTraversal.setPath("parent");
        entityParentTraversal.setName("entityParentTraversal");
        SelectionSpec selSpec = new SelectionSpec();
        selSpec.setName("entityParentTraversal");
        entityParentTraversal.getSelectSet().add(selSpec);

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(morEntity);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(entityParentTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> ocs = context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        assert (ocs != null && ocs.size() > 0);
        assert (ocs.get(0).getObj() != null);
        assert (ocs.get(0).getPropSet().get(0) != null);
        assert (ocs.get(0).getPropSet().get(0).getVal() != null);

        String dcName = ocs.get(0).getPropSet().get(0).getVal().toString();
        return new Pair<>(new DatacenterMO(context, ocs.get(0).getObj()), dcName);
    }

    public ManagedObjectReference getDvPortGroupMor(String dvPortGroupName) throws Exception {
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("DistributedVirtualPortgroup");
        pSpec.getPathSet().add("name");

        TraversalSpec datacenter2DvPortGroupTraversal = new TraversalSpec();
        datacenter2DvPortGroupTraversal.setType("Datacenter");
        datacenter2DvPortGroupTraversal.setPath("network");
        datacenter2DvPortGroupTraversal.setName("datacenter2DvPortgroupTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(datacenter2DvPortGroupTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> ocs = context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                List<DynamicProperty> props = oc.getPropSet();
                if (props != null) {
                    for (DynamicProperty prop : props) {
                        if (prop.getVal().equals(dvPortGroupName)) {
                            return oc.getObj();
                        }
                    }
                }
            }
        }
        return null;
    }

    public ManagedObjectReference getDvSwitchMor(ManagedObjectReference dvPortGroupMor) throws Exception {
        String dvPortGroupKey = null;
        ManagedObjectReference dvSwitchMor = null;
        PropertySpec pSpec = new PropertySpec();
        pSpec.setType("DistributedVirtualPortgroup");
        pSpec.getPathSet().add("key");
        pSpec.getPathSet().add("config.distributedVirtualSwitch");

        TraversalSpec datacenter2DvPortGroupTraversal = new TraversalSpec();
        datacenter2DvPortGroupTraversal.setType("Datacenter");
        datacenter2DvPortGroupTraversal.setPath("network");
        datacenter2DvPortGroupTraversal.setName("datacenter2DvPortgroupTraversal");

        ObjectSpec oSpec = new ObjectSpec();
        oSpec.setObj(mor);
        oSpec.setSkip(Boolean.TRUE);
        oSpec.getSelectSet().add(datacenter2DvPortGroupTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pSpec);
        pfSpec.getObjectSet().add(oSpec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> ocs = context.getService().retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                List<DynamicProperty> props = oc.getPropSet();
                if (props != null) {
                    assert (props.size() == 2);
                    for (DynamicProperty prop : props) {
                        if ("key".equals(prop.getName())) {
                            dvPortGroupKey = (String)prop.getVal();
                        } else {
                            dvSwitchMor = (ManagedObjectReference)prop.getVal();
                        }
                    }
                    if ((dvPortGroupKey != null) && dvPortGroupKey.equals(dvPortGroupMor.getValue())) {
                        return dvSwitchMor;
                    }
                }
            }
        }
        return null;
    }

    public String getDvSwitchUuid(ManagedObjectReference dvSwitchMor) throws Exception {
        assert (dvSwitchMor != null);
        return (String) context.getVimClient().getDynamicProperty(dvSwitchMor, "uuid");
    }

    public VirtualEthernetCardDistributedVirtualPortBackingInfo getDvPortBackingInfo(Pair<ManagedObjectReference, String> networkInfo) throws Exception {
        assert (networkInfo != null);
        assert (networkInfo.first() != null && "DistributedVirtualPortgroup".equalsIgnoreCase(networkInfo.first().getType()));
        final VirtualEthernetCardDistributedVirtualPortBackingInfo dvPortBacking = new VirtualEthernetCardDistributedVirtualPortBackingInfo();
        final DistributedVirtualSwitchPortConnection dvPortConnection = new DistributedVirtualSwitchPortConnection();
        ManagedObjectReference dvsMor = getDvSwitchMor(networkInfo.first());
        String dvSwitchUuid = getDvSwitchUuid(dvsMor);
        dvPortConnection.setSwitchUuid(dvSwitchUuid);
        dvPortConnection.setPortgroupKey(networkInfo.first().getValue());
        dvPortBacking.setPort(dvPortConnection);
        System.out.println("Plugging NIC device into network " + networkInfo.second() + " backed by dvSwitch: " + dvSwitchUuid);
        return dvPortBacking;
    }



    public DatacenterConfigInfo getDatacenterConfigInfo() throws Exception {
        DatacenterConfigInfo configInfo =  context.getVimClient().getDynamicProperty(mor, "configuration");
        return configInfo;
    }
}

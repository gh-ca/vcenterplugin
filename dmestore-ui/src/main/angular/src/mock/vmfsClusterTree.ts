export const vmfsClusterTreeData = [
  {
    clusterId: 'urn:vmomi:ClusterComputeResource:domain-c1087:674908e5-ab21-4079-9cb1-596358ee5dd1',
    clusterName: 'baa',
    deviceType:'cluster',
    children: [],
    flag:true
  },
  {
    clusterId: 'urn:vmomi:ClusterComputeResource:domain-c2063:674908e5-ab21-4079-9cb1-596358ee5dd1',
    clusterName: 'ghca_cluster02',
    deviceType:'cluster',
    flag:true,
    children: [
      {
        clusterId: 'urn:vmomi:HostSystem:host-2055:674908e5-ab21-4079-9cb1-596358ee5dd1',
        clusterName: '10.143.133.197',
        deviceType:'host',
        children: null,
        flag:true
      },
      {
        clusterId: 'urn:vmomi:HostSystem:host-2056:674908e5-ab21-4079-9cb1-596358ee5dd1',
        clusterName: '10.143.133.197',
        deviceType:'host',
        children: null,
        flag:false
      },
    ],
  },
  {
    clusterId: 'urn:vmomi:ClusterComputeResource:domain-c2044:674908e5-ab21-4079-9cb1-596358ee5dd1',
    clusterName: 'lq_datacenter',
    deviceType:'cluster',
    flag: true,
    children: [
      {
        clusterId: 'urn:vmomi:HostSystem:host-16256:674908e5-ab21-4079-9cb1-596358ee5dd1',
        clusterName: '10.143.133.196',
        deviceType:'host',
        children: null,
        flag: false,
      },
    ],
  },
  {
    clusterId: 'urn:vmomi:ClusterComputeResource:domain-c2038:674908e5-ab21-4079-9cb1-596358ee5dd1',
    clusterName: 'ghca_cluseter01',
    deviceType:'cluster',
    flag: true,
    children: [
      {
        clusterId: 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1',
        clusterName: '10.143.133.17',
        deviceType:'host',
        children: null,
        flag: true,
      },
      {
        clusterId: 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1',
        clusterName: '10.143.133.17',
        deviceType:'host',
        children: null,
        flag: true,
      },
    ],
  },
];

export const responseVmfsClusterTreeData = {
  code: '200',
  data: vmfsClusterTreeData,
  description: null,
};

export const getVmfsDmestorageStorageByTag = tag => ({
  code: '200',
  data: {
    qosFlag: tag > 0,
    id: '4e3109c2-91fa-11eb-bb86-7e7d8e1bd299',
    name: 'Huawei.Storage',
    ip: '10.143.133.201',
    status: '1',
    synStatus: '2',
    sn: '2102351QLH9WK5800028',
    vendor: 'Huawei',
    model: '5300 V5',
    usedCapacity: 2162.541999816406,
    totalCapacity: 6542.693389892578,
    totalEffectiveCapacity: 3236.0,
    freeEffectiveCapacity: 1073.4569999999999,
    subscriptionCapacity: 3.3568372353e7,
    protectionCapacity: 0.0,
    fileCapacity: 254.417,
    blockCapacity: 1908.1260000000002,
    blockFileCapacity: 0.0,
    dedupedCapacity: 0.0,
    compressedCapacity: 0.0,
    optimizeCapacity: 2162.541999816406,
    azIds: [],
    storagePool: null,
    volume: null,
    fileSystem: null,
    dTrees: null,
    nfsShares: null,
    bandPorts: null,
    logicPorts: null,
    storageControllers: null,
    storageDisks: null,
    productVersion: 'V500R007C10',
    warning: null,
    event: null,
    location: 'aaaa',
    patchVersion: 'SPH013',
    maintenanceStart: null,
    maintenanceOvertime: null,
    storageTypeShow: {
      qosTag: tag,
      workLoadShow: 2,
      ownershipController: true,
      allocationTypeShow: 1,
      deduplicationShow: true,
      compressionShow: true,
      capacityInitialAllocation: true,
      smartTierShow: true,
      prefetchStrategyShow: true,
      storageDetailTag: 2,
      dorado: false,
    },
    // smartQos: {
    //   name: '',
    //   latency: '',
    //   maxbandwidth: '',
    //   maxiops: '',
    //   minbandwidth: '',
    //   miniops: '',
    //   enabled: '',
    //   controlPolicy: '',
    //   latencyUnit: '',
    // },
    smartQos: {
      name: '',
      latency: 50,
      maxbandwidth: "",
      maxiops: "",
      minbandwidth: 100,
      miniops: 100,
      enabled: '',
      controlPolicy: '',
      latencyUnit: '',
    },
  },
  description: null,
});
export default getVmfsDmestorageStorageByTag(2);
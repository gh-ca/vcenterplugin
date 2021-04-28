export const STATUS_ARRAY = [
  { value: '', label: 'vmfs.filter.all', color: '#A9A9A9' },
  { value: 1, label: 'storage.list.normal', color: '#7CDFA0' },
  { value: 0, label: 'storage.list.Offline', color: '#A9A9A9' },
  { value: 2, label: 'storage.list.Abnormal', color: 'red' },
  { value: 9, label: 'storage.list.Unmanaged', color: 'yellow' },
  { value: -1, label: 'storage.list.Unknown', color: '#A9A9A9' },
  { value: 5, label: 'storage.list.degraded', color: '#A9A9A9' },
];

export const SYN_STATUS_ARRAY = [
  { value: '', label: 'vmfs.filter.all', color: '#A9A9A9' },
  { value: 0, label: 'storage.list.unsync', color: '#A9A9A9' },
  { value: 1, label: 'storage.list.Sync', color: '#00BFFF' },
  { value: 2, label: 'storage.list.synced', color: '#7CDFA0' },
  { value: 3, label: 'storage.list.syncFailed', color: 'red' },
  { value: -1, label: 'storage.list.Unknown', color: '#A9A9A9' },
];

const data = [
  {
    id: '4e3109c2-91fa-11eb-bb86-7e7d8e1bd299',
    name: 'Huawei.Storage',
    ip: '10.143.133.201',
    status: '1',
    synStatus: '2',
    vendor: 'Huawei',
    model: '5300 V5',
    usedCapacity: 0,
    totalCapacity: 6699718.03125,
    totalEffectiveCapacity: 0.0,
    freeEffectiveCapacity: 0.0,
    maxCpuUtilization: 4.1,
    maxIops: 65.0,
    maxBandwidth: 1.8,
    maxLatency: 3.86,
    maxOps: null,
    azIds: [],
    djAzs: [
      { id: 1, name: 'one' },
      { id: 2, name: 'two' },
    ],
    sn: '2102351QLH9WK5800028',
    version: 'V500R007C10',
    productVersion: null,
    totalPoolCapacity: 3258368.0,
    subscriptionCapacity: 3.43739149854712e10,
    capacityUtilization: null,
    subscriptionRate: null,
    location: '',
    patchVersion: 'SPH013',
    maintenanceStart: null,
    maintenanceOvertime: null,
    storageTypeShow: {
      /* v6 */
      qosTag: 1,
      workLoadShow: 2,
      ownershipController: true,
      allocationTypeShow: 1,
      deduplicationShow: true,
      compressionShow: true,
      capacityInitialAllocation: true,
      smartTierShow: true,
      prefetchStrategyShow: true,
      storageDetailTag: 2,
      dorado: true,
    },
  },
  {
    id: '4e3109c2-91fa-11eb-bb86-7e7d8e1bd299_mock',
    name: 'tencent.storage',
    ip: '10.143.133.201',
    status: '1',
    synStatus: '2',
    vendor: 'Huawei',
    model: '5300 V5',
    usedCapacity: 2135851.007812,
    totalCapacity: 6699718.03125,
    totalEffectiveCapacity: 0.0,
    freeEffectiveCapacity: 0.0,
    maxCpuUtilization: 4.1,
    maxIops: 65.0,
    maxBandwidth: 1.8,
    maxLatency: 3.86,
    maxOps: null,
    azIds: [],
    djAzs: [
      { id: 1, name: 'one' },
      { id: 2, name: 'two' },
    ],
    sn: '2102351QLH9WK5800028_mock',
    version: 'V500R007C10',
    productVersion: null,
    totalPoolCapacity: 3258368.0,
    subscriptionCapacity: 3.43739149854712e10,
    capacityUtilization: null,
    subscriptionRate: null,
    location: '',
    patchVersion: 'SPH013',
    maintenanceStart: null,
    maintenanceOvertime: null,
    storageTypeShow: {
      qosTag: 2,
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
  },
];
export const getList = count => {
  return {
    code: '200',
    data: [
      ...data,
      ...[...new Array(count)].map((i, ii) => {
        return {
          ...data[0],
          id: data[0].id + ii,
          name: data[0].name + ii,
          usedCapacity: Math.random() * 2135851.007812 + 1,
        };
      }),
    ],
    description: null,
  };
};

const statusArray = STATUS_ARRAY.map(i => i.value);
const statusLength = statusArray.length;
const synStatusArray = SYN_STATUS_ARRAY.map(i => i.value);
const synStatusLength = synStatusArray.length;

export const getDmestorageStoragesManystate = count => {
  return {
    code: '200',
    data: [
      ...data,
      ...[...new Array(count)].map((i, ii) => {
        const status = statusArray[ii % statusLength];
        const synStatus = synStatusArray[ii % synStatusLength];

        console.log(status, synStatus);
        return {
          ...data[0],
          status,
          synStatus,
          id: data[0].id + ii,
          name: data[0].name + ii,
          usedCapacity: Math.random() * 2135851.007812 + 1,
        };
      }),
    ],
    description: null,
  };
};
export default {
  code: '200',
  data,
  description: null,
};

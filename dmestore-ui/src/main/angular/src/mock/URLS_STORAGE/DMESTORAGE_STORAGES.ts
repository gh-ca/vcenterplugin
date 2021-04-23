import { STATUS_ARRAY, SYN_STATUS_ARRAY } from './../../app/routes/storage/filter.component';

const data = [
  {
    id: '4e3109c2-91fa-11eb-bb86-7e7d8e1bd299',
    name: 'Huawei.Storage',
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

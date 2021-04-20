const data = {
  name: 'DataStore ',
  totalCapacity: 100,
  usedCapacity: 70,
  freeCapacity: 30,
  utilization: 20,
  capacityUnit: 'TB',
};

export default {
  code: '200',
  data: [...new Array(5)].map((i, ii) => {
    const totalCapacity = data.totalCapacity - ii*10;
    const usedCapacity = data.usedCapacity - ii*10;

    return {
      ...data,
      name: data.name + (ii + 1),
      totalCapacity,
      usedCapacity,
      utilization: (usedCapacity / totalCapacity) * 100,
    };
  }),
  description: null,
};

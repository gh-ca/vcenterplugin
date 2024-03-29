package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim.binding.vim.ManagedEntity;
import com.vmware.vim25.*;

import java.util.List;

/**
 * IscsiManagerMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class PerformanceManagerMo extends BaseMo {

    public PerformanceManagerMo(VmwareContext context, ManagedObjectReference performancemanagermo) {
        super(context, performancemanagermo);
    }

    public List<PerfEntityMetricBase> queryPerf(List<PerfQuerySpec> perfQuerySpecs) throws Exception {
       return context.getService().queryPerf(mor, perfQuerySpecs);
    }

    public List<PerfCounterInfo> queryPerfCounter(List<Integer> list) throws Exception {
        return context.getService().queryPerfCounter(mor, list);
    }

    public List<PerfCounterInfo> queryPerfCounterByLevel(int i) throws Exception {
        return context.getService().queryPerfCounterByLevel(mor, i);
    }

    public List<PerfMetricId> queryAvailablePerfMetric(ManagedObjectReference entity, Integer refreshRate) throws Exception{
        return context.getService().queryAvailablePerfMetric(mor, entity, null, null, refreshRate);
    }
}

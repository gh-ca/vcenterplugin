package com.dmeplugin.dmestore.services;

import java.util.Map;

/**
 * Created by hyuan on 2017/5/10.
 */
public interface DmeAccessService {
    /**
     * Access DME
     */
    Map<String, Object> accessDme(Map<String, Object> params);

    /**
     * Refresh connection status
     */
    Map<String, Object> refreshDme();

}

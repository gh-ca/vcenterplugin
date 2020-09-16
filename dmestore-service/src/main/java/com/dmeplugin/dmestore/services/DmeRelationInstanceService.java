package com.dmeplugin.dmestore.services;

import com.dmeplugin.dmestore.model.RelationInstance;

import java.util.List;

/**
 * @Description: TODO
 * @ClassName: DmeRelationInstanceService
 * @Company: GH-CA
 * @author: liuxh
 * @create: 2020-09-15
 **/
public interface DmeRelationInstanceService {

    /**
     * 按资源关系类型名称查询资源关系实例
     * @param relationName enum:M_DjTierContainsLun M_DjTierContainsStoragePool M_DjTierContainsStoragePort
     * @return
     * @throws Exception
     */
    List<RelationInstance> queryRelationByRelationName(String relationName) throws Exception;

    /**
     * 按资源关系类型名称和条件sourceInstanceId查询资源关系实例
     * @param relationName 资源关系类型名称
     * @param sourceInstanceId 源实例ID
     * @return
     */
    List<RelationInstance> queryRelationByRelationNameConditionSourceInstanceId(String relationName, String sourceInstanceId) throws Exception;

    /**
     * 按资源关系类型名称和实例ID查询资源关系实例
     * @param relationName enum:M_DjTierContainsLun M_DjTierContainsStoragePool M_DjTierContainsStoragePort
     * @param instanceId uuid
     * @return
     * @throws Exception
     */
    RelationInstance queryRelationByRelationNameInstanceId(String relationName, String instanceId) throws Exception;
}

package com.example.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.modules.sys.entity.SysDeptEntity;

import java.util.List;

/**
 * 部门管理
 */
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);

}

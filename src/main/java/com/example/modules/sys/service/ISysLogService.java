package com.example.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.modules.sys.entity.SysLogEntity;
import com.example.common.utils.PageUtils;

import java.util.List;
import java.util.Map;


/**
 * 系统日志
 */
public interface ISysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    boolean deleteAll();

    List<SysLogEntity> getIndexInterfaces();
}

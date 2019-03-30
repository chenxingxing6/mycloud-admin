package com.example.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.utils.PageUtils;
import com.example.modules.sys.entity.SysDictEntity;

import java.util.Map;

/**
 * 数据字典
 */
public interface ISysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


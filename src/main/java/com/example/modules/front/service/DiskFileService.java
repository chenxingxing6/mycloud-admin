package com.example.modules.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.utils.PageUtils;
import com.example.modules.front.entity.DiskFileEntity;

import java.util.Map;

/**
 * 企业共享网盘和文件对应关系
 *
 * @author lanxinghua
 * @date 2019-04-02 22:27:52
 */
public interface DiskFileService extends IService<DiskFileEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


package com.example.modules.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.utils.PageUtils;
import com.example.modules.front.entity.SysDiskEntity;

import java.util.List;
import java.util.Map;

/**
 * 企业网盘目录
 *
 * @author lanxinghua
 * @date 2019-04-02 22:27:52
 */
public interface SysDiskService extends IService<SysDiskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SysDiskEntity> listSysDisksByCompanyId(Long companyId);
}


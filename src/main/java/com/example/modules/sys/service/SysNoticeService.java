package com.example.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.utils.PageUtils;
import com.example.modules.sys.entity.SysNoticeEntity;

import java.util.Map;

/**
 * 通知公告表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-04-06 20:36:01
 */
public interface SysNoticeService extends IService<SysNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


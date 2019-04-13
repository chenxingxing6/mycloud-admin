package com.example.modules.front.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.common.constants.UserEnum;
import com.example.modules.sys.entity.SysNoticeEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysUserService;
import com.example.modules.sys.service.SysNoticeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/4/12 12:50
 * Desc: 前台系统通知
 */
@RestController
@RequestMapping("/front/app")
public class AppNoticeController {
    @Autowired
    private SysNoticeService sysNoticeService;

    /**
     * 获取系统通知
     * @param type
     * @return
     */
    @RequestMapping(value = "/listNotice")
    List<SysNoticeEntity> listNotice(String type){
        return sysNoticeService.selectList(new EntityWrapper<SysNoticeEntity>()
        .eq("status", 0).and()  //状态正常
        .eq(StringUtils.isNotEmpty(type),"notice_type", type)
        .orderBy("create_time", false));
    }
}

package com.example.modules.front.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.common.constants.UserEnum;
import com.example.common.utils.DateUtils;
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
     * 获取系统通知 (1通知 2公告）
     * @param type
     * @return
     */
    @RequestMapping(value = "/listNotice")
    List<SysNoticeEntity> listNotice(String type, String title, String date){
        Long start = null;
        Long end = null;
        if (StringUtils.isNotEmpty(date)){
            String startTime = date.split("~")[0];
            String endTime = date.split("~")[1];
            start = DateUtils.stringToTimestamp(startTime, "yyyy-MM-dd");
            end = DateUtils.stringToTimestamp(endTime, "yyyy-MM-dd");
        }
        EntityWrapper<SysNoticeEntity> ew = new EntityWrapper<>();
        ew.eq("status", 0);
        ew.like(StringUtils.isNotEmpty(title), "notice_title", title);
        ew.between(start!=null,"create_time", start, end);
        ew.orderBy("create_time", false);
        System.out.println(ew.getSqlSegment());
        return sysNoticeService.selectList(ew);
    }
}

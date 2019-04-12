package com.example.modules.front.controller;

import com.example.common.constants.UserEnum;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/4/12 12:50
 * Desc: 前台用户登录
 */
@RestController
@RequestMapping("/front")
public class LoginController {
    @Autowired
    private ISysUserService userService;

    @RequestMapping(value = "/getUserByAccount")
    public SysUserEntity getUserByAccount(String username, String password){
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return getUser(map);
    }

    @RequestMapping(value = "/getUserByMobile")
    public SysUserEntity getUserByMobile(String mobile){
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        return getUser(map);
    }

    private SysUserEntity getUser(Map<String, Object> map){
        //用户状态
        map.put("status", 1);
        //前台用户
        map.put("type", UserEnum.FRONT.getType());
        List<SysUserEntity> userEntityList = userService.selectByMap(map);
        if (CollectionUtils.isEmpty(userEntityList)){
            return null;
        }
        return userEntityList.get(0);
    }
}

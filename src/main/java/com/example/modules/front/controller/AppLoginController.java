package com.example.modules.front.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.common.constants.UserEnum;
import com.example.common.validator.Assert;
import com.example.modules.sys.entity.SysDeptEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysDeptService;
import com.example.modules.sys.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/4/12 12:50
 * Desc: 前台用户登录
 */
@RestController
@RequestMapping("/front/app")
public class AppLoginController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService sysDeptService;

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
        SysUserEntity sysUserEntity = userEntityList.get(0);
        //添加部门
        SysDeptEntity deptEntity = sysDeptService.selectById(Long.valueOf(sysUserEntity.getDeptId()));
        sysUserEntity.setDeptName(deptEntity == null ? "" : deptEntity.getName());
        return sysUserEntity;
    }

    /**
     * 查询
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserByUserId")
    SysUserEntity getUserByUserId(String userId){
        Assert.isBlank(userId, "参数错误");
        return userService.selectById(Long.valueOf(userId));
    }

    /**
     * 模糊查询
     * @param username
     * @return
     */
    @RequestMapping(value = "/findUser")
    List<SysUserEntity> findUserByUserName(String username){
        if (StringUtils.isEmpty(username)){
            return new ArrayList<>();
        }
        return userService.selectList(new EntityWrapper<SysUserEntity>()
        .eq("status", 1).and()
        .like("username", username)
        .orderBy("create_time", false));
    }
}

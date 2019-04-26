package com.example.modules.front.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.common.constants.UserEnum;
import com.example.common.exception.BizException;
import com.example.common.utils.R;
import com.example.common.validator.Assert;
import com.example.modules.oss.cloud.OSSFactory;
import com.example.modules.oss.entity.SysOssEntity;
import com.example.modules.oss.service.ISysOssService;
import com.example.modules.sys.entity.SysDeptEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysDeptService;
import com.example.modules.sys.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
    @Autowired
    private ISysOssService sysOssService;

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

    @RequestMapping(value = "/getUserByOpenId")
    SysUserEntity getUserByOpenId(@RequestParam("openId") String openId){
        Assert.isBlank(openId, "参数错误");
        Map<String, Object> map = new HashMap<>();
        //用户状态
        map.put("status", 1);
        //前台用户
        map.put("type", UserEnum.FRONT.getType());
        map.put("open_id", openId);
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
        SysUserEntity sysUserEntity = userService.selectById(Long.valueOf(userId));
        //添加部门
        SysDeptEntity deptEntity = sysDeptService.selectById(Long.valueOf(sysUserEntity.getDeptId()));
        sysUserEntity.setDeptName(deptEntity == null ? "" : deptEntity.getName());
        return sysUserEntity;
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


    /**
     * 更新头像
     * @param avatar
     * @return
     */
    @PostMapping(value = "/updateImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R updateImg(@RequestPart("avatar") MultipartFile avatar){
        if (avatar.isEmpty()) {
            return R.error("上传文件不能为空");
        }
        try {
            //上传文件
            String suffix = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf("."));
            String url = OSSFactory.build().uploadSuffix(avatar.getBytes(), suffix);
            //保存文件信息
            SysOssEntity ossEntity = new SysOssEntity();
            ossEntity.setUrl(url);
            ossEntity.setCreateDate(new Date());
            sysOssService.insert(ossEntity);
            return R.ok().put("url", url);
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }


    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateUser")
    void updateUser(@RequestBody SysUserEntity user){
        userService.update(user);
    }
}

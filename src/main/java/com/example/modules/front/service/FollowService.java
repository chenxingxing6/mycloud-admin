package com.example.modules.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.utils.PageUtils;
import com.example.modules.front.entity.FollowEntity;
import com.example.modules.front.vo.FollowUser;
import com.example.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 关注用户表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
public interface FollowService extends IService<FollowEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未关注的用户
     * @param userId
     * @return
     */
    List<SysUserEntity> listFollowUser(Long userId);

    /**
     * 查询已关注的用户
     * @param userId
     * @return
     */
    List<SysUserEntity> listFollowedUser(Long userId);
}


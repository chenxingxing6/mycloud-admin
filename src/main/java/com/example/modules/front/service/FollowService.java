package com.example.modules.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.utils.PageUtils;
import com.example.modules.front.entity.FollowEntity;
import com.example.modules.front.vo.FollowUser;

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
     * @param params
     * @param userId
     * @return
     */
    List<FollowUser> listFollowUser(Map<String, Object> params, Long userId);

    /**
     * 查询已关注的用户
     * @param params
     * @param userId
     * @return
     */
    List<FollowUser> listFollowedUser(Map<String, Object> params, Long userId);
}


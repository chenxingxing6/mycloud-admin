package com.example.modules.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.utils.PageUtils;
import com.example.modules.front.entity.ShareEntity;

import java.util.List;
import java.util.Map;

/**
 * 分享表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
public interface ShareService extends IService<ShareEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分享列表
     * @param fromUserId
     * @param toUserId
     * @param page
     * @param limit
     * @return
     */
    public List<ShareEntity> listShareByUserIdWithPage(Long fromUserId, Long toUserId, int page, int limit);


    /**
     * 获取分享列表总条数
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public int getShareTotalByUserId(Long fromUserId, Long toUserId);
}


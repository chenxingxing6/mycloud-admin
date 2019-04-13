package com.example.modules.front.dao;

import com.example.modules.front.entity.ShareEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分享表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
public interface ShareDao extends BaseMapper<ShareEntity> {
    /**
     * 获取分享列表
     * @param fromUserId
     * @param toUserId
     * @param page
     * @param pageSize
     * @return
     */
    public List<ShareEntity> listShareByUserIdWithPage(@Param("fromUserId") Long fromUserId,
                                                       @Param("toUserId") Long toUserId,
                                                       @Param(value = "page") Integer page,
                                                       @Param(value = "pageSize") Integer pageSize);


    /**
     * 获取分享列表总条数
     * @param fromUserId
     * @param toUserId
     * @return
     */
    public int getShareTotalByUserId(@Param("fromUserId") Long fromUserId,
                                     @Param("toUserId") Long toUserId);
}

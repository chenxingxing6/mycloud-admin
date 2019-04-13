package com.example.modules.front.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.modules.front.dao.ShareDao;
import com.example.modules.front.entity.ShareEntity;
import com.example.modules.front.service.ShareService;


@Service("shareService")
public class ShareServiceImpl extends ServiceImpl<ShareDao, ShareEntity> implements ShareService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ShareEntity> page = this.selectPage(
                new Query<ShareEntity>(params).getPage(),
                new EntityWrapper<ShareEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<ShareEntity> listShareByUserIdWithPage(Long fromUserId, Long toUserId, int page, int limit) {
        page = page >0 ? page -1 : 0;
        return baseMapper.listShareByUserIdWithPage(fromUserId, toUserId, page, limit);
    }

    @Override
    public int getShareTotalByUserId(Long fromUserId, Long toUserId) {
        return baseMapper.getShareTotalByUserId(fromUserId, toUserId);
    }
}

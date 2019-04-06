package com.example.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.modules.sys.dao.SysNoticeDao;
import com.example.modules.sys.entity.SysNoticeEntity;
import com.example.modules.sys.service.SysNoticeService;


@Service("sysNoticeService")
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeDao, SysNoticeEntity> implements SysNoticeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysNoticeEntity> page = this.selectPage(
                new Query<SysNoticeEntity>(params).getPage(),
                new EntityWrapper<SysNoticeEntity>()
        );

        return new PageUtils(page);
    }

}

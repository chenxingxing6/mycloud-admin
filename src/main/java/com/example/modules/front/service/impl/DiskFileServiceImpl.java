package com.example.modules.front.service.impl;

import com.example.modules.front.dao.DiskFileDao;
import com.example.modules.front.entity.DiskFileEntity;
import com.example.modules.front.service.DiskFileService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;



@Service("diskFileService")
public class DiskFileServiceImpl extends ServiceImpl<DiskFileDao, DiskFileEntity> implements DiskFileService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiskFileEntity> page = this.selectPage(
                new Query<DiskFileEntity>(params).getPage(),
                new EntityWrapper<DiskFileEntity>()
        );

        return new PageUtils(page);
    }

}

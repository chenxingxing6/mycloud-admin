package com.example.modules.front.service.impl;

import com.example.modules.front.dao.SysDiskDao;
import com.example.modules.front.entity.SysDiskEntity;
import com.example.modules.front.service.SysDiskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;



@Service("sysDiskService")
public class SysDiskServiceImpl extends ServiceImpl<SysDiskDao, SysDiskEntity> implements SysDiskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<SysDiskEntity> page = this.selectPage(
                new Query<SysDiskEntity>(params).getPage(),
                new EntityWrapper<SysDiskEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SysDiskEntity> listSysDisksByCompanyId(Long companyId) {
        return this.selectList(new EntityWrapper<SysDiskEntity>()
        .eq(companyId !=null, "company_id", companyId));
    }
}

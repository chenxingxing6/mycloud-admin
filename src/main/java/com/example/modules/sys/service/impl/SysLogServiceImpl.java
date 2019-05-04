package com.example.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.utils.Query;
import com.example.common.utils.PageUtils;
import com.example.modules.sys.dao.SysLogDao;
import com.example.modules.sys.entity.SysLogEntity;
import com.example.modules.sys.service.ISysLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements ISysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");
        Page<SysLogEntity> page = this.selectPage(
            new Query<SysLogEntity>(params).getPage(),
            new EntityWrapper<SysLogEntity>().like(StringUtils.isNotBlank(key),"username", key)
        );
        return new PageUtils(page);
    }


    @Override
    public boolean deleteAll() {
        this.delete(new Wrapper<SysLogEntity>() {
            @Override
            public String getSqlSegment() {
                return null;
            }
        });
        return true;
    }

    @Override
    public List<SysLogEntity> getIndexInterfaces() {
        return this.selectList(new EntityWrapper<SysLogEntity>()
        .orderBy("time", false)
        .last("limit 5"));
    }
}

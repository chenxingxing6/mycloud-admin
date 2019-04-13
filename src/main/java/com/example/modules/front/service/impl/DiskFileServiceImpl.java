package com.example.modules.front.service.impl;

import com.example.modules.front.dao.DiskFileDao;
import com.example.modules.front.entity.DiskFileEntity;
import com.example.modules.front.service.DiskFileService;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<DiskFileEntity> listDiskAllFile(Long diskId) {
        return this.selectList(new EntityWrapper<DiskFileEntity>()
        .eq(diskId !=null, "disk_id", diskId)
        .eq("is_valid", 1)
        .orderBy("create_time", Boolean.FALSE));
    }

    @Override
    public void deleteByFileId(Long fileId) {
        this.delete(new EntityWrapper<DiskFileEntity>()
        .eq(fileId !=null, "file_id", fileId));
    }
}

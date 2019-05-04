package com.example.modules.front.service.impl;

import com.example.common.constants.FileEnum;
import com.example.common.utils.DateUtils;
import com.example.common.utils.IdGen;
import com.example.modules.front.dao.HdfsDao;
import com.example.modules.front.entity.UserFileEntity;
import com.example.modules.front.service.UserFileService;
import com.example.modules.front.vo.FileVo;
import com.example.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.modules.front.dao.FileDao;
import com.example.modules.front.entity.FileEntity;
import com.example.modules.front.service.FileService;

import javax.annotation.Resource;


@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileDao, FileEntity> implements FileService {
    @Resource
    private IdGen idGen;
    @Resource
    private UserFileService userFileService;
    @Resource
    private HdfsDao hdfsDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long userId) {
        Query query = new Query<FileEntity>(params);
        long parentId = Long.valueOf(params.get("parentId").toString());
        List<FileEntity> fileEntities = getFileList(userId, parentId);
        return new PageUtils(fileEntities, fileEntities.size(), query.getLimit(), query.getCurrPage());
    }

    @Override
    public List<FileEntity> getFileList(long userId, long parentId) {
        List<UserFileEntity> userFileEntities = userFileService.getFilesByUserId(userId);
        if (CollectionUtils.isEmpty(userFileEntities)){
            return new ArrayList<>();
        }
        List<Long> fileIds = userFileEntities.stream().map(e->e.getFileId()).collect(Collectors.toList());
        List<FileEntity> fileEntities = this.selectBatchIds(fileIds);
        return fileEntities.stream().filter(e->e.getParentId().equals(parentId)).collect(Collectors.toList());
    }

    @Override
    public List<Long> getFileIds(long userId, long parentId) {
        List<UserFileEntity> userFileEntities = userFileService.getFilesByUserId(userId);
        if (CollectionUtils.isEmpty(userFileEntities)){
            return new ArrayList<>();
        }
        List<Long> fileIds = userFileEntities.stream().map(e->e.getFileId()).collect(Collectors.toList());
        return fileIds;
    }

    @Override
    public List<FileEntity> listFileByIds(List<Long> ids) {
        List<FileEntity> results = this.selectBatchIds(ids);
        if (CollectionUtils.isEmpty(results)){
            return new ArrayList<FileEntity>();
        }
        return results.stream().filter(e->e.getIsValid() == 1).collect(Collectors.toList());
    }

    @Override
    public List<FileEntity> listFileByIdsWithPage(List<Long> ids, String fileName, int page, int limit) {
        page = page>0 ? page -1 : 0;
        return baseMapper.listFileByIdsWithPage(ids,fileName, page, limit);
    }

    @Override
    public int getFileTotalByIds(List<Long> ids, String fileName) {
        return baseMapper.getFileTotalByIdsWithPage(ids, fileName);
    }

    @Override
    public void makeFolder(FileEntity file, SysUserEntity user, long parentid) {
        //用户文件关联表
        UserFileEntity userFileEntity = new UserFileEntity();
        userFileEntity.setId(idGen.nextId());
        userFileEntity.setFileId(file.getId());
        userFileEntity.setUserId(user.getUserId());
        userFileService.insert(userFileEntity);

        //hdfs上传文件夹
        hdfsDao.mkDir(file, user);

        //文件表
        this.insert(file);
    }


    @Override
    public void deleteHdfs(SysUserEntity user, FileEntity file) {
        hdfsDao.delete(file, user);
    }

    @Override
    public void deleteFileRecursion(SysUserEntity user, FileEntity file) {
        if (file.getType().equals(FileEnum.FILE.getType())){
            this.deleteById(file.getId());
            userFileService.delete(new EntityWrapper<UserFileEntity>().eq("user_id", user.getUserId()).and().eq("file_id", file.getId()));
            return;
        }
        //文件夹
        else if (file.getType().equals(FileEnum.FOLDER.getType())){
            //获取该文件下的子文件
            List<FileEntity> fileEntities = getFileList(user.getUserId(), file.getId());
            //该目录下没有文件，删除目录
            if (CollectionUtils.isEmpty(fileEntities)){
                this.deleteById(file.getId());
                userFileService.delete(new EntityWrapper<UserFileEntity>().eq("user_id", user.getUserId()).and().eq("file_id", file.getId()));
                return;
            }
            for (FileEntity subFile : fileEntities) {
                if (subFile.getType().equals(FileEnum.FILE)){
                    deleteFileRecursion(user, subFile);
                }
                this.deleteById(subFile.getId());
                userFileService.delete(new EntityWrapper<UserFileEntity>().eq("user_id", user.getUserId()).and().eq("file_id", subFile.getId()));
            }
        }
    }

    @Override
    public void deleteFile(Long userId, Long fileId) {
        this.deleteById(fileId);
        userFileService.delete(new EntityWrapper<UserFileEntity>().eq("user_id", userId).and().eq("file_id", fileId));
        return;
    }

    @Override
    public void uploadFile(InputStream inputStream, FileEntity file, SysUserEntity user) {
        //hdfs上传文件
        hdfsDao.put(inputStream, file, user);
        //文件表
        this.insert(file);
    }

    @Override
    public boolean downloadFile(SysUserEntity user, FileEntity file, String localPath) {
        return hdfsDao.download(user, file, localPath);
    }

    @Override
    public List<FileEntity> getFileDowns() {
        return this.selectList(new EntityWrapper<FileEntity>()
        .eq("type", 1)
        .orderBy("download_num", false)
        .orderBy("create_time", false)
        .last("limit 5"));
    }

    @Override
    public int getTodayDiskNum() {
        long time = DateUtils.dateToStamp(DateUtils.format(new Date(), "yyyyMMdd"), "yyyyMMdd");
        return this.selectCount(new EntityWrapper<FileEntity>()
                .eq("type", 1)
                .eq("is_valid", 1)
                .gt("create_time",time)
        );
    }

    @Override
    public int getTotalDiskNum() {
        return this.selectCount(new EntityWrapper<FileEntity>()
        .eq("type", 1)
        .eq("is_valid", 1));
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.format(new Date(), "yyyyMMdd"));
        System.out.println(DateUtils.dateToStamp(DateUtils.format(new Date(), "yyyyMMdd"), "yyyyMMdd"));
    }
}

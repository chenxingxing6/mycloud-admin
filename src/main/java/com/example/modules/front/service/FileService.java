package com.example.modules.front.service;

import com.baomidou.mybatisplus.service.IService;
import com.example.common.constants.FileEnum;
import com.example.common.utils.PageUtils;
import com.example.modules.front.entity.FileEntity;
import com.example.modules.sys.entity.SysUserEntity;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 用户与文件对应关系
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
public interface FileService extends IService<FileEntity> {

    PageUtils queryPage(Map<String, Object> params, Long userId);

    /**
     * 获取文件列表，查看文件或目录列表
     *
     * @param userId
     * @param parentId
     * @return
     */
    public List<FileEntity> getFileList(long userId, long parentId);


    /**
     * 获取文件ids列表
     *
     * @param userId
     * @param parentId
     * @return
     */
    public List<Long> getFileIds(long userId, long parentId);


    /**
     * 获取文件列表,批量查询
     *
     * @param ids
     * @return
     */
    public List<FileEntity> listFileByIds(List<Long> ids);


    /**
     * 获取文件列表,分页
     * @param ids
     * @param page
     * @param limit
     * @return
     */
    public List<FileEntity> listFileByIdsWithPage(List<Long> ids, String fileName, int page, int limit);


    /**
     * 获取文件列表总条数
     * @param ids
     * @return
     */
    public int getFileTotalByIds(List<Long> ids, String fileName);


    /**
     * 创建文件夹
     * @param file
     * @param user
     * @param parentid
     */
    public void makeFolder(FileEntity file, SysUserEntity user, long parentid);

    /**
     * 删除hdfs中的文件，删除文件或目录时使用
     * @param user
     * @param file
     */
    public void deleteHdfs(SysUserEntity user, FileEntity file);

    /**
     * 递归删除file表和user_file表的文件信息，删除文件或目录时使用
     * @param user
     * @param file
     */
    public void deleteFileRecursion(SysUserEntity user, FileEntity file);

    /**
     * 删除用户文件
     * @param userId
     * @param fileId
     */
    public void deleteFile(Long userId, Long fileId);


    /**
     * 上传文件 (tb_file  hdfs)
     * @param inputStream
     * @param file
     * @param user
     */
    public void uploadFile(InputStream inputStream, FileEntity file, SysUserEntity user);


    /**
     * hdfs下载文件到服务器
     *
     * @param user
     * @param file
     * @param localPath
     * @return
     */
    public boolean downloadFile(SysUserEntity user, FileEntity file, String localPath);


    /**
     * 获取文件下载列表，首页使用
     * @return
     */
    public List<FileEntity> getFileDowns();


    public int getTodayDiskNum();

    public int getTotalDiskNum();

}


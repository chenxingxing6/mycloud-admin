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
    public List<FileEntity> listFileByIdsWithPage(List<Long> ids, int page, int limit);


    /**
     * 获取文件列表总条数
     * @param ids
     * @return
     */
    public int getFileTotalByIdsWithPage(List<Long> ids);


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

}


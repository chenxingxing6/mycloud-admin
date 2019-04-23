package com.example.modules.front.controller;

import com.example.common.constants.FileEnum;
import com.example.common.constants.ViewEnum;
import com.example.common.utils.DateUtils;
import com.example.common.utils.FilesUtil;
import com.example.common.utils.IdGen;
import com.example.common.utils.R;
import com.example.common.validator.Assert;
import com.example.modules.front.entity.DiskFileEntity;
import com.example.modules.front.entity.FileEntity;
import com.example.modules.front.entity.SysDiskEntity;
import com.example.modules.front.entity.UserFileEntity;
import com.example.modules.front.service.DiskFileService;
import com.example.modules.front.service.FileService;
import com.example.modules.front.service.SysDiskService;
import com.example.modules.front.service.UserFileService;
import com.example.modules.front.vo.DiskDirVo;
import com.example.modules.front.vo.FileVo;
import com.example.modules.oss.cloud.OSSFactory;
import com.example.modules.oss.entity.SysOssEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysDeptService;
import com.example.modules.sys.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 我的网盘 提供给前台接口
 *
 * @author 蓝星花
 * @email lanxinghua@2dfire.com
 * @date 2019-04-02 22:27:52
 */
@RestController
@RequestMapping("front/app")
public class AppFileController {
    private static final Logger logger = LoggerFactory.getLogger(AppFileController.class);

    final static IdGen idGen = IdGen.get();
    @Autowired
    private SysDiskService sysDiskService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private FileService fileService;
    @Autowired
    private DiskFileService diskFileService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private UserFileService userFileService;

    //文件下载路径
    @Value("${file.dir}")
    String fileDir;



    /**
     * 我的文件列表
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/listFileByPage")
    List<FileEntity> listFileByPage(@RequestParam("userId") String userId,
                                @RequestParam("fileParentId") String fileParentId,
                                @RequestParam("page") Integer page,
                                @RequestParam("limit") Integer limit){
        Assert.isBlank(userId, "参数错误");
        Assert.isNull(page, "参数错误");
        Assert.isNull(limit, "参数错误");
        fileParentId = StringUtils.isEmpty(fileParentId) ? "0" : fileParentId;
        List<Long> ids = fileService.getFileIds(Long.valueOf(userId), Long.valueOf(fileParentId));
        if (CollectionUtils.isEmpty(ids)){
            return new ArrayList<FileEntity>();
        }
        List<FileEntity> fileEntities = fileService.listFileByIdsWithPage(ids, null, page, limit);
        //createUser 改为用户名
        List<Long> userIds = fileEntities.stream().map(e->Long.valueOf(e.getCreateUser())).collect(Collectors.toList());
        Map<Long, SysUserEntity> map = sysUserService.selectBatchIds(userIds).stream().collect(Collectors.toMap(e->e.getUserId(), e->e));
        for (FileEntity fileEntity : fileEntities) {
            SysUserEntity user = map.get(Long.valueOf(fileEntity.getCreateUser()));
            if (user !=null){
                fileEntity.setCreateUser(user.getUsername());
            }
        }
        return fileEntities;
    }


    /**
     * 获取总页数
     * @param userId
     * @param fileParentId
     * @return
     */
    @RequestMapping(value = "/listFileTotal")
    int listFileTotal(@RequestParam("userId") String userId,
                      @RequestParam("fileParentId") String fileParentId){
        Assert.isBlank(userId, "参数错误");
        fileParentId = StringUtils.isEmpty(fileParentId) ? "0" : fileParentId;
        List<Long> ids = fileService.getFileIds(Long.valueOf(userId), Long.valueOf(fileParentId));
        return fileService.getFileTotalByIds(ids, null);
    }

    /**
     * 文件重命名
     * @param fileId
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/fileRename")
    boolean fileRename(@RequestParam("fileId") String fileId,
                       @RequestParam("fileName") String fileName,
                       @RequestParam("optUserId") String optUserId){
        Assert.isBlank(fileName, "文件名不能为空");
        Assert.isBlank(fileId, "参数错误");
        FileEntity file = new FileEntity();
        file.setId(Long.valueOf(fileId));
        file.setOpTime(System.currentTimeMillis());
        file.setOpUser(optUserId);
        file.setOriginalName(fileName);
        fileService.updateById(file);
        return Boolean.TRUE;
    }


    /**
     * 下载文件
     *
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/downloadFile")
    public String downloadLocal(String fileId) {
        Assert.isBlank(fileId, "参数错误");
        return innerDownLoadFile(fileId);
    }

    private String innerDownLoadFile(String fileId){
        FileEntity file = fileService.selectById(fileId);
        if (file == null){
            return null;
        }
        String createUserId = file.getCreateUser();
        Assert.isBlank(createUserId, "文件上传没有用户ID");
        SysUserEntity createUser = sysUserService.selectById(createUserId);
        Assert.isNull(createUser, "上传用户不存在");
        String localFilePath = fileDir + file.getOriginalName();
        File localFile = new File(localFilePath);
        if (!localFile.exists()){
            File realPath = new File(fileDir);
            if(!realPath.exists()) {
                realPath.mkdirs();
            }
            if(fileService.downloadFile(createUser, file, localFilePath)) {
                return localFilePath;
            }else {
                return null;
            }
        }else {
            return localFilePath;
        }
    }



    /**
     * 删除文件
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/delFileById")
    void delFileById(@RequestParam("userId") String userId,
                     @RequestParam("fileId") String fileId){
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(fileId)){
            return;
        }
        fileService.deleteFile(Long.valueOf(userId), Long.valueOf(fileId));
    }


    /**
     * 添加到企业网盘
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/addDisk")
    void addDisk(@RequestParam("userId") String userId,
                 @RequestParam("fileId") String fileId){

        DiskFileEntity diskFileEntity = new DiskFileEntity();
        diskFileEntity.setCreateUser(userId);
        diskFileEntity.setCreateTime(System.currentTimeMillis());
        diskFileEntity.setDiskId(Long.valueOf(1));
        diskFileEntity.setFileId(Long.valueOf(fileId));
        diskFileService.insert(diskFileEntity);
    }


    /**
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/getDiskFileByFileId")
    DiskFileEntity getDiskFileByFileId(@RequestParam("fileId") String fileId){
        Map<String, Object> map = new HashMap<>();
        map.put("file_id", fileId);
        map.put("is_valid", 1);
        List<DiskFileEntity> entities = diskFileService.selectByMap(map);
        if (CollectionUtils.isEmpty(entities)){
            return null;
        }
        return entities.get(0);
    }


    /**
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/getFileByFileId")
    FileEntity getFileByFileId(@RequestParam("fileId") String fileId){
        Map<String, Object> map = new HashMap<>();
        map.put("id", fileId);
        map.put("is_valid", 1);
        List<FileEntity> fileEntities = fileService.selectByMap(map);
        if (CollectionUtils.isEmpty(fileEntities)){
            return null;
        }
        return fileEntities.get(0);
    }



    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R uploadFile(@RequestPart("file") MultipartFile file, @RequestHeader("userId") String userId){
        if (file.isEmpty()) {
            return R.error("上传文件不能为空");
        }
        Assert.isBlank(userId, "用户信息缺失");
        try {
            InputStream is = file.getInputStream();
            uploadFile(is, file, userId);
            return R.ok();
        }catch (Exception e){
            return R.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadFile(InputStream is, MultipartFile file, String userId){
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = FilesUtil.getFileSufix(fileName);
        //创建文件
        FileEntity fileEntity = new FileEntity();
        fileEntity.setType(FileEnum.FILE.getType());
        fileEntity.setLength(FilesUtil.FormetFileSize(file.getSize()));
        fileEntity.setOriginalName(fileName);
        int splitIndex = file.getOriginalFilename().lastIndexOf(".");
        String name1 = System.nanoTime() + "." + suffixName;
        Long fileId = idGen.nextId();
        fileEntity.setId(fileId);
        fileEntity.setName(name1);
        fileEntity.setPath("/"+name1);
        fileEntity.setOriginalPath("/");
        fileEntity.setViewFlag(ViewEnum.Y.getType());
        fileEntity.setCreateUser(userId);
        fileEntity.setOpUser(userId);
        fileEntity.setCreateTime(System.currentTimeMillis());
        fileEntity.setOpTime(System.currentTimeMillis());
        SysUserEntity userEntity = sysUserService.selectById(Long.valueOf(userId));
        fileService.uploadFile(is, fileEntity, userEntity);
        //关系表添加数据
        UserFileEntity userFileEntity = new UserFileEntity();
        userFileEntity.setId(idGen.nextId());
        userFileEntity.setUserId(Long.valueOf(userId));
        userFileEntity.setFileId(fileId);
        userFileService.insert(userFileEntity);

        innerDownLoadFileByAsyn(userEntity, fileEntity);
    }

    @Async
    public void innerDownLoadFileByAsyn(SysUserEntity userEntity, FileEntity fileEntity){
        System.out.println("线程名称："+Thread.currentThread().getName() + " 下载文件!"+fileEntity.getOriginalName());
        //文件下载
        String localFilePath = fileDir + fileEntity.getOriginalName();
        File localFile = new File(localFilePath);
        if (!localFile.exists()) {
            File realPath = new File(fileDir);
            if (!realPath.exists()) {
                realPath.mkdirs();
            }
        }
        fileService.downloadFile(userEntity, fileEntity, localFilePath);
    }
}

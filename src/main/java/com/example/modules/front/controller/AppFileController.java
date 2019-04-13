package com.example.modules.front.controller;

import com.example.common.utils.DateUtils;
import com.example.common.utils.IdGen;
import com.example.common.utils.R;
import com.example.common.validator.Assert;
import com.example.modules.front.entity.DiskFileEntity;
import com.example.modules.front.entity.FileEntity;
import com.example.modules.front.entity.SysDiskEntity;
import com.example.modules.front.service.DiskFileService;
import com.example.modules.front.service.FileService;
import com.example.modules.front.service.SysDiskService;
import com.example.modules.front.vo.DiskDirVo;
import com.example.modules.front.vo.FileVo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        return fileService.listFileByIdsWithPage(ids, null, page, limit);
    }


    /**
     * 获取总页数
     * @param userId
     * @param fileParentId
     * @return
     */
    @RequestMapping(value = "/front/app/listFileTotal")
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
    @RequestMapping(value = "/front/app/fileRename")
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
}

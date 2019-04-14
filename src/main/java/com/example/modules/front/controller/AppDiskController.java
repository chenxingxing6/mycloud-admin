package com.example.modules.front.controller;

import com.example.common.constants.FileEnum;
import com.example.common.constants.ViewEnum;
import com.example.common.exception.BizException;
import com.example.common.utils.*;
import com.example.common.validator.Assert;
import com.example.common.validator.ValidatorUtils;
import com.example.modules.front.entity.DiskFileEntity;
import com.example.modules.front.entity.FileEntity;
import com.example.modules.front.entity.SysDiskEntity;
import com.example.modules.front.service.DiskFileService;
import com.example.modules.front.service.FileService;
import com.example.modules.front.service.SysDiskService;
import com.example.modules.front.vo.DiskDirVo;
import com.example.modules.front.vo.FileVo;
import com.example.modules.sys.controller.AbstractController;
import com.example.modules.sys.entity.SysDeptEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysDeptService;
import com.example.modules.sys.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 企业共享网盘目录 提供给前台接口
 *
 * @author 蓝星花
 * @email lanxinghua@2dfire.com
 * @date 2019-04-02 22:27:52
 */
@RestController
@RequestMapping("front/app")
public class AppDiskController{
    private static final Logger logger = LoggerFactory.getLogger(AppDiskController.class);

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
     * 获取企业网盘目录类型
     * @param userId
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/listDiskDirType")
    List<DiskDirVo> listDiskDirType(String userId, String deptId){
        List<DiskDirVo> diskDirVos = new ArrayList<>();
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(deptId)){
            logger.error("参数错误");
            return diskDirVos;
        }
        //获取到顶级部门ID
        Long superDeptId = sysDeptService.getSuperDeptId(Long.valueOf(deptId));
        if (superDeptId == null){
            return diskDirVos;
        }
        List<SysDiskEntity> sysDiskEntitys = sysDiskService.listSysDisksByCompanyId(superDeptId);
        if (CollectionUtils.isEmpty(sysDiskEntitys)){
            return diskDirVos;
        }
        for (SysDiskEntity sysDiskEntity : sysDiskEntitys) {
            DiskDirVo vo = new DiskDirVo();
            vo.setName(sysDiskEntity.getName());
            vo.setType(Integer.valueOf(sysDiskEntity.getId().toString()));
            diskDirVos.add(vo);
        }
        return diskDirVos;
    }


    /**
     * 获取企业网盘资源
     * @param deptId
     * @param diskId 为0的时候查所有
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/listDisk")
    List<FileEntity> listDisk(@RequestParam("deptId") String deptId,
                          @RequestParam("diskId") String diskId,
                          @RequestParam("fileName") String fileName,
                          @RequestParam("page") Integer page,
                          @RequestParam("limit") Integer limit){
        List<FileVo> fileVos = new ArrayList<>();
        Assert.isBlank(deptId, "参数错误");
        Assert.isBlank(diskId, "参数错误");
        Assert.isNull(page, "参数错误");
        Assert.isNull(limit, "参数错误");
        List<Long> fileIds = listFileIds(diskId, deptId);
        fileName = StringUtils.isEmpty(fileName) ? null : fileName;
        List<FileEntity> fileEntities = fileService.listFileByIdsWithPage(fileIds, fileName, page, limit);
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
     * @param deptId
     * @param diskId 为0的时候查所有
     * @return
     */
    @RequestMapping(value = "/listDiskTotal")
    int listDiskTotal(@RequestParam("deptId") String deptId,
                      @RequestParam("diskId") String diskId,
                      @RequestParam("fileName") String fileName){
        Assert.isBlank(deptId, "参数错误");
        Assert.isBlank(diskId, "参数错误");
        fileName = StringUtils.isEmpty(fileName) ? null : fileName;
        List<Long> fileIds = listFileIds(diskId, deptId);
        return fileService.getFileTotalByIds(fileIds, fileName);
    }


    //获取文件id
    private List<Long> listFileIds(String diskId, String deptId){
        List<Long> ids = new ArrayList<>();
        if (diskId.equals("0")){
            //获取到顶级部门ID
            Long superDeptId = sysDeptService.getSuperDeptId(Long.valueOf(deptId));
            List<SysDiskEntity> sysDiskEntities = sysDiskService.listSysDisksByCompanyId(superDeptId);
            if (CollectionUtils.isEmpty(sysDiskEntities)){
                return ids;
            }
            for (SysDiskEntity sysDiskEntity : sysDiskEntities) {
                ids.addAll(innerListDiskFiles(Long.valueOf(sysDiskEntity.getId())));
            }
        }else {
            ids.addAll(innerListDiskFiles(Long.valueOf(diskId)));
        }
        return ids;
    }

    //通过diskId获取文件Ids
    private List<Long> innerListDiskFiles(Long diskId){
        List<DiskFileEntity> diskFileEntities = diskFileService.listDiskAllFile(diskId);
        if (CollectionUtils.isEmpty(diskFileEntities)){
            return new ArrayList<>();
        }
        return diskFileEntities.stream().map(e->e.getFileId()).distinct().collect(Collectors.toList());
    }
}

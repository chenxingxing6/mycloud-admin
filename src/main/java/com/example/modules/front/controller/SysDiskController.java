package com.example.modules.front.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.common.constants.FileEnum;
import com.example.common.constants.FileTypeEnum;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 企业共享网盘目录
 *
 * @author 蓝星花
 * @email sunlightcs@gmail.com
 * @date 2019-04-02 22:27:52
 */
@RestController
@RequestMapping("front/disk")
public class SysDiskController extends AbstractController{
    private static final Logger logger = LoggerFactory.getLogger(SysDiskController.class);
    final static IdGen idGen = IdGen.get();
    @Autowired
    private SysDiskService sysDiskService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private FileService fileService;
    @Autowired
    private DiskFileService diskFileService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:sysdisk:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysDiskService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:sysdisk:info")
    public R info(@PathVariable("id") Long id){
        SysDiskEntity sysDisk = sysDiskService.selectById(id);

        return R.ok().put("sysDisk", sysDisk);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:sysdisk:save")
    public R save(@RequestBody SysDiskEntity sysDisk){
        sysDiskService.insert(sysDisk);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:sysdisk:update")
    public R update(@RequestBody SysDiskEntity sysDisk){
        ValidatorUtils.validateEntity(sysDisk);
        sysDiskService.updateAllColumnById(sysDisk);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:sysdisk:delete")
    public R delete(@RequestBody Long[] ids){
        sysDiskService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 创建网盘目录
     */
    @RequestMapping("/createDir")
    public R createDir(@RequestBody SysDiskEntity sysDisk){
        ValidatorUtils.validateEntity(sysDisk);
        String dirName = sysDisk.getName();
        SysUserEntity user = getUser();
        if (user == null){
            throw new BizException("用户信息为空");
        }
        if (user.getDeptId() == null){
            throw new BizException("用户所在部门信息查询失败");
        }
        //获取到顶级部门ID
        Long superDeptId = sysDeptService.getSuperDeptId(user.getDeptId());
        sysDisk.setCompanyId(superDeptId);
        SysDeptEntity deptEntity = sysDeptService.selectById(superDeptId);
        if (deptEntity == null){
            throw new BizException("用户所在最顶级部门为空");
        }
        sysDisk.setCompanyName(deptEntity.getName());
        sysDisk.setCreateTime(System.currentTimeMillis());
        sysDisk.setCreateUser(user.getUserId().toString());
        sysDisk.setName(dirName);
        sysDisk.setIsValid(1);
        sysDiskService.insert(sysDisk);
        return R.ok();
    }

    /**
     * 修改网盘目录
     */
    @RequestMapping("/updateDir")
    public R updateDir(@RequestBody SysDiskEntity sysDisk){
        ValidatorUtils.validateEntity(sysDisk);
        sysDiskService.updateById(sysDisk);
        return R.ok();
    }

    /**
     * 获取网盘目录
     */
    @RequestMapping("/listDir")
    public R listDir(){
        SysUserEntity user = getUser();
        if (user == null){
            logger.warn("用户信息为空");
            return R.ok().put("diskDirs", new ArrayList<DiskDirVo>());
        }
        if (user.getDeptId() == null){
            logger.warn("用户所在部门信息查询失败");
            return R.ok().put("diskDirs", new ArrayList<DiskDirVo>());
        }
        //获取到顶级部门ID
        Long superDeptId = sysDeptService.getSuperDeptId(user.getDeptId());
        List<SysDiskEntity> sysDiskEntitys = sysDiskService.listSysDisksByCompanyId(superDeptId);
        if (CollectionUtils.isEmpty(sysDiskEntitys)){
            return R.ok().put("diskDirs", new ArrayList<DiskDirVo>());
        }
        List<DiskDirVo> diskDirVos = new ArrayList<>();
        for (SysDiskEntity sysDiskEntity : sysDiskEntitys) {
            DiskDirVo vo = new DiskDirVo();
            vo.setName(sysDiskEntity.getName());
            vo.setType(Integer.valueOf(sysDiskEntity.getId().toString()));
            diskDirVos.add(vo);
        }
        return R.ok().put("diskDirs", diskDirVos);
    }

    /**
     * 获取网盘文件列表
     * @param diskId 为0的时候查所有
     */
    @RequestMapping("/listFiles")
    public R listFileList(@RequestParam("diskId") String diskId){
        Assert.isBlank(diskId, "参数错误");
        SysUserEntity user = getUser();
        if (user == null){
            logger.warn("用户信息为空");
            return R.ok().put("files", new ArrayList<FileVo>());
        }
        List<DiskFileEntity> diskFileEntities = diskFileService.listDiskAllFile(Long.valueOf(diskId));
        if (CollectionUtils.isEmpty(diskFileEntities)){
            return R.ok().put("files", new ArrayList<FileVo>());
        }
        List<Long> fileIds = diskFileEntities.stream().map(e->e.getFileId()).distinct().collect(Collectors.toList());
        List<FileEntity> fileEntities = fileService.listFileByIds(fileIds);
        List<FileVo> fileVos = new ArrayList<>();
        for (FileEntity fileEntity : fileEntities) {
            FileVo fileVo = new FileVo();
            BeanUtils.copyProperties(fileEntity, fileVo);
            fileVo.setOpTime(DateUtils.format(fileEntity.getOpTime(), DateUtils.DATE_TIME_PATTERN));
            fileVo.setId(fileEntity.getId().toString());
            fileVo.setParentId(fileEntity.getParentId().toString());
            fileVos.add(fileVo);
        }
        return R.ok().put("files", fileVos);
    }

    @RequestMapping(value = "/uploadFile")
    public R upload(@RequestParam("file") MultipartFile file, @RequestParam("diskId")String diskId) {
        try {
            if (file.isEmpty()) {
                return R.error("文件为空");
            }
            SysUserEntity userEntity = getUser();
            InputStream is = file.getInputStream();
            uploadFile(is, file, userEntity, diskId);
            is.close();
        } catch (Exception e){
            logger.error("文件上传失败",e);
            throw new BizException("文件上传失败");
        }
        return R.ok("上传成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public void uploadFile(InputStream is, MultipartFile file, SysUserEntity userEntity, String diskId){
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
        fileEntity.setCreateUser(userEntity.getUserId().toString());
        fileEntity.setOpUser(userEntity.getUserId().toString());
        fileEntity.setCreateTime(System.currentTimeMillis());
        fileEntity.setOpTime(System.currentTimeMillis());

        fileService.uploadFile(is, fileEntity, userEntity);
        //关系表添加数据
        DiskFileEntity diskFileEntity = new DiskFileEntity();
        diskFileEntity.setCreateUser(userEntity.getUserId().toString());
        diskFileEntity.setCreateTime(System.currentTimeMillis());
        diskFileEntity.setDiskId(Long.valueOf(diskId));
        diskFileEntity.setFileId(fileId);
        diskFileService.insert(diskFileEntity);
    }
}

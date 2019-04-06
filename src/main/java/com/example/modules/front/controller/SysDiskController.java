package com.example.modules.front.controller;

import java.io.File;
import java.io.FileInputStream;
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
import com.example.modules.sys.service.ISysUserService;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


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
    @Autowired
    private ISysUserService sysUserService;

    //文件下载路径
    @Value("${file.dir}")
    String fileDir;

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
     * 删除文件 （删除关系表）
     */
    @RequestMapping("/delFile")
    public R delete(@RequestParam("fileId") String fileId){
        diskFileService.deleteByFileId(Long.valueOf(fileId));
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
        List<FileVo> fileVos = new ArrayList<>();
        if (diskId.equals("0")){
            if (user.getDeptId() == null){
                logger.warn("用户所在部门信息查询失败");
                return R.ok().put("files", fileVos);
            }
            //获取到顶级部门ID
            Long superDeptId = sysDeptService.getSuperDeptId(user.getDeptId());
            List<SysDiskEntity> sysDiskEntities = sysDiskService.listSysDisksByCompanyId(superDeptId);
            if (CollectionUtils.isEmpty(sysDiskEntities)){
                return R.ok().put("files", fileVos);
            }
            for (SysDiskEntity sysDiskEntity : sysDiskEntities) {
                fileVos.addAll(innerListDiskFiles(sysDiskEntity.getId()));
            }
        }else {
            fileVos.addAll(innerListDiskFiles(Long.valueOf(diskId)));
        }
        return R.ok().put("files", fileVos);
    }

    private List<FileVo> innerListDiskFiles(Long diskId){
        List<DiskFileEntity> diskFileEntities = diskFileService.listDiskAllFile(diskId);
        if (CollectionUtils.isEmpty(diskFileEntities)){
            return new ArrayList<>();
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
        return fileVos;
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


    /**
     * 下载文件
     *
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/downloadFile")
    public R download(@RequestParam("fileId")String fileId) {
        Assert.isBlank(fileId, "参数错误");
        return innerDownLoadFile(fileId);
    }

    private R innerDownLoadFile(String fileId){
        FileEntity file = fileService.selectById(fileId);
        if (file == null){
            return R.error("文件不存在");
        }
        String createUserId = file.getCreateUser();
        Assert.isBlank(createUserId, "文件上传没有用户ID");
        SysUserEntity createUser = sysUserService.selectById(createUserId);
        Assert.isNull(createUser, "上传用户不存在");
        SysUserEntity userEntity = getUser();
        Assert.isNull(userEntity, "用户信息错误");
        String localFilePath = fileDir + file.getOriginalName();
        File localFile = new File(localFilePath);
        if (!localFile.exists()){
            File realPath = new File(fileDir);
            if(!realPath.exists()) {
                realPath.mkdirs();
            }
            if(fileService.downloadFile(createUser, file, localFilePath)) {
                return R.ok("下载成功").put("url", file.getOriginalName());
            }else {
                return R.error("文件不存在");
            }
        }else {
            return R.ok("文件已经存在").put("url", file.getOriginalName());
        }
    }


    /**
     * 用户下载文件
     * @param response
     * @param fileId
     * @return
     */
    @RequestMapping("/userdown")
    public void downloadFile(HttpServletResponse response, @RequestParam("fileId")String fileId) {
        Assert.isBlank(fileId, "参数错误");
        R r = innerDownLoadFile(fileId);
        String originalName = r.get("url").toString();
        try {
            //下载的文件携带这个名称
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName="+ new String(originalName.getBytes("GB2312"),"ISO-8859-1"));
            String localFilePath = fileDir + originalName;
            FileInputStream fis = new FileInputStream(localFilePath);
            byte[] content = new byte[fis.available()];
            fis.read(content);
            fis.close();
            ServletOutputStream sos = response.getOutputStream();
            sos.write(content);
            sos.flush();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

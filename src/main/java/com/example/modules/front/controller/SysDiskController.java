package com.example.modules.front.controller;

import java.io.*;
import java.util.*;

import com.example.common.exception.BizException;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;
import com.example.common.validator.ValidatorUtils;
import com.example.modules.front.entity.SysDiskEntity;
import com.example.modules.front.service.SysDiskService;
import com.example.modules.front.vo.DiskDirVo;
import com.example.modules.sys.controller.AbstractController;
import com.example.modules.sys.entity.SysDeptEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysDeptService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;


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

    @Autowired
    private SysDiskService sysDiskService;
    @Autowired
    private ISysDeptService sysDeptService;

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
     * 上传文件
     */
    @RequestMapping("/uploadFile")
    public R uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file){
        try {
            System.out.println(file);
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                multipartRequest.setCharacterEncoding("UTF-8");
                Map<String, MultipartFile> fms = multipartRequest.getFileMap();
                for (Map.Entry<String, MultipartFile> entity : fms.entrySet()) {
                    MultipartFile multipartFile = entity.getValue();
                    InputStream inputStream = multipartFile.getInputStream();
                    int splitIndex = multipartFile.getOriginalFilename().lastIndexOf(".");
                    String name = System.nanoTime() + "." + multipartFile.getOriginalFilename().substring(splitIndex + 1);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return R.ok();
    }
}

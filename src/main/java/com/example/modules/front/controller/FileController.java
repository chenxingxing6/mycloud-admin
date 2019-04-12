package com.example.modules.front.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.common.constants.FileEnum;
import com.example.common.constants.ViewEnum;
import com.example.common.exception.BizException;
import com.example.common.utils.DateUtils;
import com.example.common.utils.IdGen;
import com.example.common.validator.Assert;
import com.example.common.validator.ValidatorUtils;
import com.example.modules.front.vo.FileVo;
import com.example.modules.sys.controller.AbstractController;
import com.example.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.modules.front.entity.FileEntity;
import com.example.modules.front.service.FileService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 用户与文件对应关系
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
@RestController
@RequestMapping("front/file")
public class FileController extends AbstractController {
    final static IdGen idGen = IdGen.get();

    @Autowired
    private FileService fileService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        //根据父ID,用户ID
        Long userId = getUserId();
        PageUtils page = fileService.queryPage(params, userId);
        List<FileEntity> fileEntities = (List<FileEntity>)page.getList();
        List<FileVo> fileVos = new ArrayList<>();
        for (FileEntity fileEntity : fileEntities) {
            FileVo fileVo = new FileVo();
            BeanUtils.copyProperties(fileEntity, fileVo);
            fileVo.setOpTime(DateUtils.format(fileEntity.getOpTime(), DateUtils.DATE_TIME_PATTERN));
            fileVo.setId(fileEntity.getId().toString());
            fileVo.setParentId(fileEntity.getParentId().toString());
            fileVos.add(fileVo);
        }
        page.setList(fileVos);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FileEntity file = fileService.selectById(id);

        return R.ok().put("file", file);
    }


    /**
     * 创建文件
     *
     * @param file
     * @param dirName      文件路径名   /39166745223986
     * @param originalDir  原始文件路径 /file
     * @param parentId     父文件ID
     * @return
     */
    @RequestMapping("/createFile")
    public R createFile(@RequestBody FileEntity file,
                        @RequestParam(value="dirName") String dirName,
                        @RequestParam(value="originalDir") String originalDir,
                        @RequestParam(value="parentId") String parentId){
        String fileName = file.getOriginalName();
        Assert.isBlank(fileName, "文件名不能为空");
        Long parentid = Long.valueOf(parentId);
        //封装文件对象
        file.setId(idGen.nextId());
        SysUserEntity user = getUser();
        file.setType(FileEnum.FOLDER.getType());
        //另取文件名
        String name = System.nanoTime() + "";
        file.setName(name);
        file.setOriginalName(fileName);
        file.setParentId(parentid);
        file.setLength("0");
        file.setViewFlag(ViewEnum.N.getType());
        file.setCreateTime(System.currentTimeMillis());
        file.setCreateUser(user.getUserId().toString());
        file.setOpTime(System.currentTimeMillis());
        file.setOpUser(user.getUserId().toString());
        if (dirName.equals("/")){
            file.setPath(dirName + name);
            file.setOriginalPath(originalDir + fileName);
        }else {
            file.setPath(dirName + "/"+ name);
            file.setOriginalPath(originalDir +"/"+ fileName);
        }
        R r = new R();
        List<FileEntity> fileList = fileService.getFileList(user.getUserId(), parentid);
        if (CollectionUtils.isEmpty(fileList)){
            fileService.makeFolder(file, user, parentid);
            r.put("msg", "创建文件夹成功");
        }else {
            Boolean flag = true;
            for (FileEntity fileEntity : fileList) {
                if (fileEntity.getType().equals(FileEnum.FOLDER.getType()) && fileEntity.getOriginalName().equals(file.getOriginalName())){
                    flag = false;
                    break;
                }
            }
            if (flag){
                fileService.makeFolder(file, user, parentid);
                r.put("msg", "创建文件夹成功");
            }else {
                throw new BizException("文件夹已经存在");
            }
        }
        return r;
    }

    /**
     * 修改文件或文件夹
     */
    @RequestMapping("/updateFile")
    public R updateFile(@RequestBody FileEntity file){
        if (StringUtils.isEmpty(file.getOriginalName())){
            throw new BizException("文件名不能为空");
        }
        file.setOpTime(System.currentTimeMillis());
        file.setOpUser(getUserId().toString());
        fileService.updateById(file);
        return R.ok();
    }

    /**
     * 修改文件名
     */
    @RequestMapping("/updateFileName")
    public R updateFileName(@RequestParam("fileId") String fileId, @RequestParam("fileName") String fileName){
        Assert.isBlank(fileId, "参数错误");
        Assert.isBlank(fileName, "文件不能为空");
        FileEntity file = new FileEntity();
        file.setId(Long.valueOf(fileId));
        file.setOpTime(System.currentTimeMillis());
        file.setOpUser(getUserId().toString());
        file.setOriginalName(fileName);
        fileService.updateById(file);
        return R.ok();
    }

    /**
     * 删除文件或者文件夹
     * @param ids
     * @return
     */
    @RequestMapping("/deleteFileOrFolder")
    public R deleteFileOrFolder(@RequestBody Long[] ids) {
        if (ids.length ==0){
            throw new BizException("删除条数为空");
        }
        SysUserEntity user = getUser();
        R r = new R();
        try {
            List<Long> idList = Arrays.asList(ids);
            List<FileEntity> files = fileService.selectBatchIds(idList);
            Map<Long/*fileId*/, FileEntity> fileEntityMap = files.stream().collect(Collectors.toMap(FileEntity::getId, e->e));
            for (Long id : idList) {
                if (fileEntityMap.get(id) == null){
                    continue;
                }
                FileEntity file = fileEntityMap.get(id);

                //删除Hdfs中文件
                fileService.deleteHdfs(user, file);

                //递归删除file和user_file文件
                fileService.deleteFileRecursion(user, file);
            }
            r.put("msg", "删除成功！");
        } catch (Exception e) {
            r.put("code", 500);
            r.put("msg", "删除失败！");
            e.printStackTrace();
        }
        return r;
    }
}

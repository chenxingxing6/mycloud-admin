package com.example.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import com.example.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.modules.sys.entity.SysNoticeEntity;
import com.example.modules.sys.service.SysNoticeService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 通知公告表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-04-06 20:36:01
 */
@RestController
@RequestMapping("sys/sysnotice")
public class SysNoticeController extends AbstractController{
    @Autowired
    private SysNoticeService sysNoticeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:sysnotice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysNoticeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:sysnotice:info")
    public R info(@PathVariable("id") Long id){
        SysNoticeEntity sysNotice = sysNoticeService.selectById(id);

        return R.ok().put("sysNotice", sysNotice);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:sysnotice:save")
    public R save(@RequestBody SysNoticeEntity sysNotice){
        Long userId = getUserId();
        sysNotice.setCreateUser(userId.toString());
        sysNotice.setCreateTime(System.currentTimeMillis());
        sysNoticeService.insert(sysNotice);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:sysnotice:update")
    public R update(@RequestBody SysNoticeEntity sysNotice){
        ValidatorUtils.validateEntity(sysNotice);
        sysNoticeService.updateAllColumnById(sysNotice);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:sysnotice:delete")
    public R delete(@RequestBody Long[] ids){
        sysNoticeService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}

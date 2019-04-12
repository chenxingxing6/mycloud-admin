package com.example.modules.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.common.utils.DateUtils;
import com.example.common.validator.Assert;
import com.example.common.validator.ValidatorUtils;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.entity.server.Sys;
import com.example.modules.sys.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:sysnotice:list")
    public R list(@RequestParam Map<String, Object> params){
        Object optUserName = params.get("optuser");
        if (optUserName !=null && StringUtils.isNotEmpty(optUserName.toString())){
            SysUserEntity user = sysUserService.queryByName(optUserName.toString());
            if (user == null){
                return R.ok();
            }
            params.put("createUser", user.getUserId());
        }
        PageUtils page = sysNoticeService.queryPage(params);
        //创建者姓名修改一下
        List<SysNoticeEntity> list = (List<SysNoticeEntity>)page.getList();
        if (CollectionUtils.isEmpty(list)){
            return R.ok().put("page", page);
        }
        List<Long> userIds = list.stream().map(e->Long.valueOf(e.getCreateUser())).collect(Collectors.toList());
        List<SysUserEntity> userEntities = sysUserService.selectBatchIds(userIds);
        if (CollectionUtils.isEmpty(userEntities)){
            return R.ok().put("page", page);
        }
        Map<Long, SysUserEntity> userMap = userEntities.stream().collect(Collectors.toMap(e->e.getUserId(), e->e));
        for (Object o : page.getList()) {
            SysNoticeEntity noticeEntity = (SysNoticeEntity) o;
            SysUserEntity user = userMap.get(Long.valueOf(noticeEntity.getCreateUser()));
            noticeEntity.setNewTime(DateUtils.format(noticeEntity.getCreateTime(), DateUtils.DATE_TIME_PATTERN));
            noticeEntity.setCreateUser(user == null ? "" : user.getUsername());
        }
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
        ValidatorUtils.validateEntity(sysNotice);
        SysUserEntity user = getUser();
        Assert.isNull(user, "用户信息获取失败");
        sysNotice.setCreateUser(user.getUserId().toString());
        sysNotice.setCreateTime(System.currentTimeMillis());
        sysNotice.setOpUser(user.getUserId().toString());
        sysNotice.setOpTime(System.currentTimeMillis());
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
        SysUserEntity user = getUser();
        Assert.isNull(user, "用户信息获取失败");
        sysNotice.setOpUser(user.getUserId().toString());
        sysNotice.setOpTime(System.currentTimeMillis());
        sysNoticeService.updateById(sysNotice);//全部更新
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

    /**
     * 删除
     */
    @RequestMapping("/deleteById")
    public R deleteById(@RequestParam("id") String id){
        sysNoticeService.deleteBatchIds(Arrays.asList(Long.valueOf(id)));
        return R.ok();
    }
}

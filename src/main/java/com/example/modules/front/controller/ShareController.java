package com.example.modules.front.controller;

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

import com.example.modules.front.entity.ShareEntity;
import com.example.modules.front.service.ShareService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 分享表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
@RestController
@RequestMapping("front/share")
public class ShareController {
    @Autowired
    private ShareService shareService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("front:share:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = shareService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("front:share:info")
    public R info(@PathVariable("id") Long id){
        ShareEntity share = shareService.selectById(id);

        return R.ok().put("share", share);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("front:share:save")
    public R save(@RequestBody ShareEntity share){
        shareService.insert(share);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("front:share:update")
    public R update(@RequestBody ShareEntity share){
        ValidatorUtils.validateEntity(share);
        shareService.updateAllColumnById(share);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("front:share:delete")
    public R delete(@RequestBody Long[] ids){
        shareService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}

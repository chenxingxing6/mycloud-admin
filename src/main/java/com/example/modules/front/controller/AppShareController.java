package com.example.modules.front.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.common.validator.Assert;
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
 * 前台分享
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
@RestController
@RequestMapping("front/app")
public class AppShareController {
    @Autowired
    private ShareService shareService;

    /**
     * 分享列表
     * @param userId
     * @param type   0:我的分享  1:我收到的分享
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/listShare")
    List<ShareEntity> listShare(@RequestParam("userId") String userId,
                                @RequestParam("type") String type,
                                @RequestParam("page") Integer page,
                                @RequestParam("limit") Integer limit){
        Assert.isBlank(userId, "参数错误");
        Assert.isBlank(type, "参数错误");
        Assert.isNull(page, "参数错误");
        Assert.isNull(limit, "参数错误");
        List<ShareEntity> list = new ArrayList<>();
        if ("0".equals(type)){
            list = shareService.listShareByUserIdWithPage(Long.valueOf(userId), null, page, limit);
        }else if ("1".equals(type)){
            list = shareService.listShareByUserIdWithPage(null, Long.valueOf(userId), page, limit);
        }
        return list;
    }


    @RequestMapping(value = "/listShareTotal")
    int listShareTotal(@RequestParam("userId") String userId,
                       @RequestParam("type") String type){
        Assert.isBlank(userId, "参数错误");
        Assert.isBlank(type, "参数错误");
        int num = 0;
        if ("0".equals(type)){
            num = shareService.getShareTotalByUserId(Long.valueOf(userId), null);
        }else if ("1".equals(type)){
            num = shareService.getShareTotalByUserId(null, Long.valueOf(userId));
        }
        return num;
    }


   /* @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = shareService.queryPage(params);

        return R.ok().put("page", page);
    }


    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShareEntity share = shareService.selectById(id);

        return R.ok().put("share", share);
    }


    @RequestMapping("/save")
    public R save(@RequestBody ShareEntity share){
        shareService.insert(share);

        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody ShareEntity share){
        ValidatorUtils.validateEntity(share);
        shareService.updateAllColumnById(share);//全部更新

        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        shareService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }*/

}

package com.example.modules.front.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.common.validator.ValidatorUtils;
import com.example.modules.front.vo.FollowUser;
import com.example.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.modules.front.entity.FollowEntity;
import com.example.modules.front.service.FollowService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 关注用户表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
@RestController
@RequestMapping("front/follow")
public class FollowController extends AbstractController {
    @Autowired
    private FollowService followService;

    /**
     * 列表（已关注和未关注）
     */
    @RequestMapping("/list")
    @RequiresPermissions("front:follow:list")
    public R list(@RequestParam Map<String, Object> params){
        Long userId = getUserId();
        List<FollowUser> follows = followService.listFollowUser(params, userId);
        List<FollowUser> followeds = followService.listFollowedUser(params, userId);
        R result = R.ok();
        result.put("follow", follows);
        result.put("followeds", followeds);
        return result;
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("front:follow:info")
    public R info(@PathVariable("id") Long id){
        FollowEntity follow = followService.selectById(id);

        return R.ok().put("follow", follow);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("front:follow:save")
    public R save(@RequestBody FollowEntity follow){
        followService.insert(follow);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("front:follow:update")
    public R update(@RequestBody FollowEntity follow){
        ValidatorUtils.validateEntity(follow);
        followService.updateAllColumnById(follow);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("front:follow:delete")
    public R delete(@RequestBody Long[] ids){
        followService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}

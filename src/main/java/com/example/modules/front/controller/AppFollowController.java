package com.example.modules.front.controller;

import java.util.*;

import com.example.common.validator.Assert;
import com.example.common.validator.ValidatorUtils;
import com.example.modules.front.vo.FollowUser;
import com.example.modules.sys.controller.AbstractController;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysUserService;
import org.apache.commons.collections.CollectionUtils;
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
@RequestMapping("front/app")
public class AppFollowController extends AbstractController {
    @Autowired
    private FollowService followService;
    @Autowired
    private ISysUserService sysUserService;


    /**
     * 获取关注，未关注用户
     * @param userId
     * @param type   0：已关注  1：未关注
     * @return
     */
    @RequestMapping(value = "/front/app/listFollowUser")
    List<FollowUser> listFollowUser(@RequestParam("userId") String userId,
                                       @RequestParam("type") String type){
        Assert.isBlank(userId, "参数错误");
        Assert.isBlank(type, "参数错误");
        List<FollowUser> followUsers = new ArrayList<>();
        if ("0".equals(type)){
            followUsers = followService.listFollowedUser(Long.valueOf(userId));
        }else if ("1".equals(type)){
            followUsers = followService.listFollowUser(Long.valueOf(userId));
        }
        return followUsers;

    }
    /**
     * 列表（已关注和未关注）
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        Long userId = getUserId();
        List<FollowUser> follows = followService.listFollowUser(userId);
        List<FollowUser> followeds = followService.listFollowedUser(userId);
        R result = R.ok();
        result.put("follow", follows);
        result.put("followeds", followeds);
        return result;
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FollowEntity follow = followService.selectById(id);

        return R.ok().put("follow", follow);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FollowEntity follow){
        followService.insert(follow);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody FollowEntity follow){
        ValidatorUtils.validateEntity(follow);
        followService.updateAllColumnById(follow);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        followService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}

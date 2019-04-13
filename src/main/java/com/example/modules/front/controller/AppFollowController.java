package com.example.modules.front.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.example.common.validator.Assert;
import com.example.common.validator.ValidatorUtils;
import com.example.modules.front.vo.FollowUser;
import com.example.modules.sys.controller.AbstractController;
import com.example.modules.sys.entity.SysDeptEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysDeptService;
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
    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 获取关注，未关注用户
     * @param userId
     * @param type   0：已关注  1：未关注
     * @return
     */
    @RequestMapping(value = "/listFollowUser")
    List<SysUserEntity> listFollowUser(@RequestParam("userId") String userId,
                                       @RequestParam("type") String type){
        Assert.isBlank(userId, "参数错误");
        Assert.isBlank(type, "参数错误");
        List<SysUserEntity> followUsers = new ArrayList<>();
        if ("0".equals(type)){
            followUsers = followService.listFollowedUser(Long.valueOf(userId));
        }else if ("1".equals(type)){
            followUsers = followService.listFollowUser(Long.valueOf(userId));
        }
        if (CollectionUtils.isEmpty(followUsers)){
            return followUsers;
        }
        List<Long> allDeptIds = followUsers.stream().map(e->e.getDeptId()).collect(Collectors.toList());
        List<SysDeptEntity> deptEntities = sysDeptService.selectBatchIds(allDeptIds);
        if (CollectionUtils.isEmpty(deptEntities)){
            return followUsers;
        }
        Map<Long, SysDeptEntity> map = deptEntities.stream().collect(Collectors.toMap(e->e.getDeptId(), e->e));
        for (SysUserEntity followUser : followUsers) {
            SysDeptEntity deptEntity = map.get(followUser.getDeptId());
            followUser.setDeptName(deptEntity == null ? "" : deptEntity.getName());
        }
        return followUsers;

    }
    /**
     * 列表（已关注和未关注）
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        Long userId = getUserId();
        List<SysUserEntity> follows = followService.listFollowUser(userId);
        List<SysUserEntity> followeds = followService.listFollowedUser(userId);
        R result = R.ok();
        result.put("follow", follows);
        result.put("followeds", followeds);
        return result;
    }


    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        FollowEntity follow = followService.selectById(id);

        return R.ok().put("follow", follow);
    }


    @RequestMapping("/save")
    public R save(@RequestBody FollowEntity follow){
        followService.insert(follow);

        return R.ok();
    }

    /*@RequestMapping("/update")
    public R update(@RequestBody FollowEntity follow){
        ValidatorUtils.validateEntity(follow);
        followService.updateAllColumnById(follow);//全部更新

        return R.ok();
    }


    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        followService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }*/

}

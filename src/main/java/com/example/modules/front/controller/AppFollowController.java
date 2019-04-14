package com.example.modules.front.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.example.common.utils.IdGen;
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
    final static IdGen idGen = IdGen.get();
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
     * 关注或取消关注
     * @param fromUserId
     * @param toUserId
     * @param type   0: 关注  1:取消关注
     * @return
     */
    @RequestMapping(value = "/addOrCancleUser")
    void addOrCancleUser(@RequestParam("fromUserId") String fromUserId,
                         @RequestParam("toUserId") String toUserId,
                         @RequestParam("type") String type){
        Assert.isBlank(fromUserId, "参数错误");
        Assert.isBlank(toUserId, "参数错误");
        Assert.isBlank(type, "参数错误");
        //关注
        if ("0".equals(type)){
            FollowEntity entity = new FollowEntity();
            entity.setId(idGen.nextId());
            entity.setFromUserId(Long.valueOf(fromUserId));
            entity.setToUserId(Long.valueOf(toUserId));
            entity.setCreateUser(fromUserId);
            entity.setCreateTime(System.currentTimeMillis());
            entity.setOpUser(fromUserId);
            entity.setOpTime(System.currentTimeMillis());
            followService.insert(entity);
        }
        //取消关注
        else if ("1".equals(type)){
            Map<String, Object> map = new HashMap<>();
            map.put("from_user_id", fromUserId);
            map.put("to_user_id", toUserId);
            followService.deleteByMap(map);
        }
    }
}

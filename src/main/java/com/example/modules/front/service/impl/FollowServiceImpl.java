package com.example.modules.front.service.impl;

import com.example.common.constants.UserEnum;
import com.example.modules.front.vo.FollowUser;
import com.example.modules.sys.dao.SysUserDao;
import com.example.modules.sys.entity.SysUserEntity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.modules.front.dao.FollowDao;
import com.example.modules.front.entity.FollowEntity;
import com.example.modules.front.service.FollowService;

import javax.annotation.Resource;


@Service("followService")
public class FollowServiceImpl extends ServiceImpl<FollowDao, FollowEntity> implements FollowService {
    @Resource
    private SysUserDao sysUserDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<FollowEntity> page = this.selectPage(
                new Query<FollowEntity>(params).getPage(),
                new EntityWrapper<FollowEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<SysUserEntity> listFollowUser(Long userId) {
        List<SysUserEntity> resultList = new ArrayList<>();
        List<FollowEntity> followEntities = innerListFollowEntity(userId);
        //已关注的用户
        List<Long> followedUserIds = followEntities.stream().map(e->e.getToUserId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(followedUserIds)){
            followedUserIds = new ArrayList<>();
        }
        //自己本身也加上
        followedUserIds.add(userId);

        //查询出所有的用户
        List<SysUserEntity> allUserEntitys = sysUserDao.selectList(new EntityWrapper<SysUserEntity>()
            .eq("type", UserEnum.FRONT.getType())
            .and().eq("status", 1)
        );
        Map<Long, SysUserEntity> map = allUserEntitys.stream().collect(Collectors.toMap(SysUserEntity::getUserId, e -> e));
        List<Long> allUserIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(allUserEntitys)){
            allUserIds = allUserEntitys.stream().map(e->e.getUserId()).collect(Collectors.toList());
        }
        allUserIds.removeAll(followedUserIds);
        if (CollectionUtils.isEmpty(allUserIds)){
            return resultList;
        }

        for (Long id : allUserIds) {
            if (map.get(id) == null){
                continue;
            }
            SysUserEntity sysUserEntity = map.get(id);
            resultList.add(sysUserEntity);
        }
        return resultList;
    }

    @Override
    public List<SysUserEntity> listFollowedUser(Long userId) {
        List<SysUserEntity> resultList = new ArrayList<>();
        List<FollowEntity> followEntities = innerListFollowEntity(userId);
        if (CollectionUtils.isEmpty(followEntities)){
            return resultList;
        }
        List<Long> ids = followEntities.stream().map(e -> e.getToUserId()).collect(Collectors.toList());
        return sysUserDao.selectBatchIds(ids);
    }

    //获取关注的用户
    private List<FollowEntity> innerListFollowEntity(Long userId){
       return this.selectList(new EntityWrapper<FollowEntity>()
               .eq(userId !=null, "from_user_id", userId)
               .eq("is_valid", 1)
               .orderBy("create_time", false)
       );
    }
}

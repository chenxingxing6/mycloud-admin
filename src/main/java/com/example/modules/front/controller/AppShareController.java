package com.example.modules.front.controller;

import java.util.*;

import com.example.common.utils.DateUtils;
import com.example.common.utils.IdGen;
import com.example.common.validator.Assert;
import com.example.common.validator.ValidatorUtils;
import com.example.modules.front.entity.FileEntity;
import com.example.modules.front.service.FileService;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysUserService;
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
    final static IdGen idGen = IdGen.get();
    @Autowired
    private ShareService shareService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private FileService fileService;

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


    /**
     * 分享给用户
     * @param fromUserId
     * @param toUserId
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/toShare")
    void toShare(@RequestParam("fromUserId") String fromUserId,
                @RequestParam("toUserId") String toUserId,
                @RequestParam("fileId") String fileId){
        Assert.isBlank(fileId, "参数错误");
        ShareEntity entity = new ShareEntity();
        entity.setId(idGen.nextId());
        entity.setFromUserId(Long.valueOf(fromUserId));
        entity.setToUserId(Long.valueOf(toUserId));

        SysUserEntity fromUser = userService.selectById(Long.valueOf(fromUserId));
        SysUserEntity toUser = userService.selectById(Long.valueOf(toUserId));
        entity.setFromUserName(fromUser == null ? "" : fromUser.getUsername());
        entity.setToUserName(toUser == null ? "" : toUser.getUsername());
        entity.setFileId(Long.valueOf(fileId));

        FileEntity file = fileService.selectById(Long.valueOf(fileId));
        entity.setFileName(file == null ? "" : file.getOriginalName());
        entity.setExpiredTime(DateUtils.getExpireTime(new Date(), 1));
        entity.setCreateUser(fromUserId);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setOpUser(fromUserId);
        entity.setOpTime(System.currentTimeMillis());
        shareService.insert(entity);
    }

    /**
     * 删除分享
     * @param shareId
     * @return
     */
    @RequestMapping(value = "/delByShareId")
    void delByShareId(@RequestParam("shareId") String shareId){
        Assert.isBlank(shareId, "参数错误");
        shareService.deleteById(Long.valueOf(shareId));
    }
}

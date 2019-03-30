package com.example.modules.front.vo;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/3/18 20:10
 * Desc: 关注用户
 */
public class FollowUser implements Serializable {

    /**
     * 用户ID
     */
    private Long followUserId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String imgPath;

    public Long getFollowUserId() {
        return followUserId;
    }

    public void setFollowUserId(Long followUserId) {
        this.followUserId = followUserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

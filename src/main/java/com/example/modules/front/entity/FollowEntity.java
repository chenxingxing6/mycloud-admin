package com.example.modules.front.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * 关注用户表
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
@TableName("tb_follow")
public class FollowEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.INPUT)
	private Long id;
	/**
	 * 用户ID
	 */
	private Long fromUserId;
	/**
	 * 被关注用户ID
	 */
	private Long toUserId;
	/**
	 * 是否有效
	 */
	private Integer isValid;
	/**
	 * 登录者
	 */
	private String createUser;
	/**
	 * 登录时间
	 */
	private Long createTime;
	/**
	 * 更新者
	 */
	private String opUser;
	/**
	 * 更新时间
	 */
	private Long opTime;
	/**
	 * 版本号
	 */
	private Integer lastVer;

	/**
	 * 设置：ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户ID
	 */
	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getFromUserId() {
		return fromUserId;
	}
	/**
	 * 设置：被关注用户ID
	 */
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	/**
	 * 获取：被关注用户ID
	 */
	public Long getToUserId() {
		return toUserId;
	}
	/**
	 * 设置：是否有效
	 */
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	/**
	 * 获取：是否有效
	 */
	public Integer getIsValid() {
		return isValid;
	}
	/**
	 * 设置：登录者
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * 获取：登录者
	 */
	public String getCreateUser() {
		return createUser;
	}
	/**
	 * 设置：登录时间
	 */
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：登录时间
	 */
	public Long getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：更新者
	 */
	public void setOpUser(String opUser) {
		this.opUser = opUser;
	}
	/**
	 * 获取：更新者
	 */
	public String getOpUser() {
		return opUser;
	}
	/**
	 * 设置：更新时间
	 */
	public void setOpTime(Long opTime) {
		this.opTime = opTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Long getOpTime() {
		return opTime;
	}
	/**
	 * 设置：版本号
	 */
	public void setLastVer(Integer lastVer) {
		this.lastVer = lastVer;
	}
	/**
	 * 获取：版本号
	 */
	public Integer getLastVer() {
		return lastVer;
	}
}

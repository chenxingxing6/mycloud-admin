package com.example.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告表
 * 
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-04-06 20:36:01
 */
@TableName("sys_notice")
public class SysNoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 公告ID
	 */
	@TableId
	private Long id;
	/**
	 * 公告标题
	 */
	@Size(max = 100, message = "公告标题长度超过默认范围")
	@NotBlank(message = "公告标题不能为空")
	private String noticeTitle;
	/**
	 * 公告类型（1通知 2公告）
	 */
	private Integer noticeType;
	/**
	 * 公告内容
	 */
	@Size(max = 500, message = "公告内容长度超过默认范围")
	@NotBlank(message = "公告内容不能为空")
	private String noticeContent;
	/**
	 * 公告状态（0正常 1关闭）
	 */
	private Integer status;
	/**
	 * 登录者
	 */
	private String createUser;
	/**
	 * 登录时间
	 */
	private Long createTime;

	@TableField(exist=false)
	private String newTime;
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
	 * 备注
	 */
	private String remark;

	/**
	 * 设置：公告ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：公告ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：公告标题
	 */
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	/**
	 * 获取：公告标题
	 */
	public String getNoticeTitle() {
		return noticeTitle;
	}
	/**
	 * 设置：公告类型（1通知 2公告）
	 */
	public void setNoticeType(Integer noticeType) {
		this.noticeType = noticeType;
	}
	/**
	 * 获取：公告类型（1通知 2公告）
	 */
	public Integer getNoticeType() {
		return noticeType;
	}
	/**
	 * 设置：公告内容
	 */
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	/**
	 * 获取：公告内容
	 */
	public String getNoticeContent() {
		return noticeContent;
	}
	/**
	 * 设置：公告状态（0正常 1关闭）
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：公告状态（0正常 1关闭）
	 */
	public Integer getStatus() {
		return status;
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
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	public String getNewTime() {
		return newTime;
	}

	public void setNewTime(String newTime) {
		this.newTime = newTime;
	}
}

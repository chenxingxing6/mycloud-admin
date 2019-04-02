package com.example.modules.front.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 企业网盘目录
 * 
 * @author lanxinghua
 * @date 2019-04-02 22:27:52
 */
@TableName("sys_disk")
public class SysDiskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 企业名字
	 */
	private String companyName;
	/**
	 * 企业ID,dept_id
	 */
	private Long companyId;
	/**
	 * 文件名
	 */
	@NotBlank(message = "名字不能为空")
	private String name;
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
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：企业名字
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * 获取：企业名字
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * 设置：企业ID,dept_id
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	/**
	 * 获取：企业ID,dept_id
	 */
	public Long getCompanyId() {
		return companyId;
	}
	/**
	 * 设置：文件名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：文件名
	 */
	public String getName() {
		return name;
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
}

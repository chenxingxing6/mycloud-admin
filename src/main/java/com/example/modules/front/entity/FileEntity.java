package com.example.modules.front.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户与文件对应关系
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
@TableName("tb_file")
public class FileEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 文件ID
	 */
	@TableId(value = "id", type = IdType.INPUT)
	private Long id;
	/**
	 * 文件父ID
	 */
	private Long parentId;
	/**
	 * 原版文件名 x9Bx98.pptx
	 */
	private String originalName;
	/**
	 * 文件名 42759866854752.pptx
	 */
	private String name;
	/**
	 * 原版文件路径 /file/
	 */
	private String originalPath;
	/**
	 * 文件路径 /39166745223986/42759866854752.pptx
	 */
	private String path;
	/**
	 * 文件长度 73.1
	 */
	private String length;
	/**
	 * 文件类型  0：目录  1：文件
	 */
	private Integer type;
	/**
	 * 是否可用看
	 */
	private Integer viewFlag;
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
	 * 下载数量
	 */
	private Integer downloadNum;

	/**
	 * 设置：文件ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：文件ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：文件父ID
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取：文件父ID
	 */
	public Long getParentId() {
		return parentId;
	}
	/**
	 * 设置：原版文件名 x9Bx98.pptx
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	/**
	 * 获取：原版文件名 x9Bx98.pptx
	 */
	public String getOriginalName() {
		return originalName;
	}
	/**
	 * 设置：文件名 42759866854752.pptx
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：文件名 42759866854752.pptx
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：原版文件路径 /file/
	 */
	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}
	/**
	 * 获取：原版文件路径 /file/
	 */
	public String getOriginalPath() {
		return originalPath;
	}
	/**
	 * 设置：文件路径 /39166745223986/42759866854752.pptx
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 获取：文件路径 /39166745223986/42759866854752.pptx
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 设置：文件长度 73.1
	 */
	public void setLength(String length) {
		this.length = length;
	}
	/**
	 * 获取：文件长度 73.1
	 */
	public String getLength() {
		return length;
	}
	/**
	 * 设置：文件类型  0：目录  1：文件
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：文件类型  0：目录  1：文件
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：是否可用看
	 */
	public void setViewFlag(Integer viewFlag) {
		this.viewFlag = viewFlag;
	}
	/**
	 * 获取：是否可用看
	 */
	public Integer getViewFlag() {
		return viewFlag;
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

	public Integer getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(Integer downloadNum) {
		this.downloadNum = downloadNum;
	}
}

package com.example.modules.front.vo;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/3/24 16:25
 * Desc:
 */
public class FileVo implements Serializable {
    private String id;
    /**
     * 文件父ID
     */
    private String parentId;
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

    private String opTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getViewFlag() {
        return viewFlag;
    }

    public void setViewFlag(Integer viewFlag) {
        this.viewFlag = viewFlag;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }
}

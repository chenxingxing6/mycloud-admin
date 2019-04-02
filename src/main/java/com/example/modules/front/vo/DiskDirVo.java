package com.example.modules.front.vo;

import java.io.Serializable;

/**
 * 网盘目录
 *
 * @Author: cxx
 * @Date: 2019/4/2 23:43
 */
public class DiskDirVo implements Serializable {
    private int type;

    private String name;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

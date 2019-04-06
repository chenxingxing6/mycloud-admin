package com.example.modules.sys.vo;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/4/6 14:16
 * Desc: 文件下载列表
 */
public class FileDownVo implements Serializable {
    //文件名
    private String name;

    //下载数量
    private int downNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }
}

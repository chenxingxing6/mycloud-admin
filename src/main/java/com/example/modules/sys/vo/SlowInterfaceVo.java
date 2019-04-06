package com.example.modules.sys.vo;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/4/6 14:17
 * Desc: 慢接口
 */
public class SlowInterfaceVo implements Serializable {
    //接口名
    private String name;

    //耗时 毫秒
    private String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

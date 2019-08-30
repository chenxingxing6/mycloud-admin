package com.example.common.face.constant;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:33
 * Desc: 操作方式
 */
public enum ActionTypeEnum {
    APPEND("重复注册"),
    REPLACE("会用新图替换");

    ActionTypeEnum(String desc){
        this.desc = desc;
    }

    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

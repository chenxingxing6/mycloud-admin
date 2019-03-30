package com.example.common.constants;

/**
 * User: lanxinghua
 * Date: 2019/3/17 15:02
 * Desc: 用户类型
 */
public enum UserEnum {
    BACK("0", "后台用户"),
    FRONT("1", "前台用户");

    UserEnum(String type, String value){
        this.type = type;
        this.value = value;
    }

    private String type;

    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

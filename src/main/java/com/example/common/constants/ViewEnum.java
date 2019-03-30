package com.example.common.constants;

/**
 * User: lanxinghua
 * Date: 2019/3/17 15:02
 * Desc: 文件是否可用查看类型
 */
public enum ViewEnum {
    N(0, "不可以"),
    Y(1, "可以");

    ViewEnum(Integer type, String value){
        this.type = type;
        this.value = value;
    }

    private Integer type;

    private String value;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

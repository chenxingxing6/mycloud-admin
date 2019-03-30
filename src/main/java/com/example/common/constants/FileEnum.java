package com.example.common.constants;

/**
 * User: lanxinghua
 * Date: 2019/3/17 15:02
 * Desc: 文件类型
 */
public enum FileEnum {
    FOLDER(0, "文件夹"),
    FILE(1, "文件");

    FileEnum(Integer type, String value){
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

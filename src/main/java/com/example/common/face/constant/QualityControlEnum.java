package com.example.common.face.constant;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:33
 * Desc: 图片质量控制
 */
public enum QualityControlEnum {
    NONE("不进行控制"),
    LOW("较低的质量要求"),
    NORMAL("一般的质量要求"),
    HIGH("较高的质量要求");

    QualityControlEnum(String desc){
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

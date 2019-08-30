package com.example.common.face.constant;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:37
 * Desc: 人脸的类型
 */
public enum FaceTypeEnum {

    LIVE("生活照"),
    IDCARD("身份证芯片照"),
    WATERMARK("水印证件照"),
    CERT("证件照片");

    FaceTypeEnum(String desc){
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

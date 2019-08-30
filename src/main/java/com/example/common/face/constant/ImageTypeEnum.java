package com.example.common.face.constant;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:26
 * Desc: 图片类型
 */
public enum  ImageTypeEnum {
    BASE64("BASE64", 2),
    URL("URL", 0),
    FACE_TOKEN("FACE_TOKEN", 0);

    ImageTypeEnum(String key, int size){
        this.key = key;
        this.size = size;
    }

    /**
     * key
     */
    private String key;

    /**
     * 大小 单位:M
     */
    private int size;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

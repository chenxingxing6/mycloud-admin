package com.example.common.constants;

/**
 * User: lanxinghua
 * Date: 2019/3/17 15:02
 * Desc: 登陆方式
 */
public enum LoginEnum {
    USER_PWD("0", "账号密码登陆"),
    WEIXIN("1", "微信登陆"),
    QQ("2", "QQ登陆"),
    GITHUB("3", "github登陆"),
    FACE("4", "人脸登陆");

    LoginEnum(String type, String value){
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

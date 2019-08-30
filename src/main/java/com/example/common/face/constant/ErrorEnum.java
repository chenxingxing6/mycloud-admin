package com.example.common.face.constant;

/**
 * User: lanxinghua
 * Date: 2019/8/30 19:09
 * Desc: 百度接口错误码,还需要加CODE看官方文档
 */
public enum  ErrorEnum {
    ERROR_ENUM_1(1, "Unknown error", "服务器内部错误，请再次请求"),
    ERROR_ENUM_13(13, "Get service token failed", "获取token失败"),
    ERROR_ENUM_222202(222202, "pic not has face", "图片中没有人脸"),
    ERROR_ENUM_222203(222203, "image check fail", "无法解析人脸"),
    ERROR_ENUM_222207(222207, "match user is not found", "未找到匹配的用户"),
    ERROR_ENUM_222209(222209, "face token not exist", "face token不存在"),
    ERROR_ENUM_222301(222301, "get face fail", "获取人脸图片失败"),
    ERROR_ENUM_223102(223102, "user is already exist", "该用户已存在"),
    ERROR_ENUM_223106(223106, "face is not exist", "该人脸不存在"),
    ERROR_ENUM_223113(223113, "face is covered", "人脸模糊"),
    ERROR_ENUM_223114(223114, "face is fuzzy", "人脸模糊"),
    ERROR_ENUM_223115(223115, "face light is not good", "人脸光照不好"),
    ERROR_ENUM_223116(223116, "incomplete face", "人脸不完整"),
    ;

    ErrorEnum(int errorCode, String desc, String cnDesc){
        this.errorCode = errorCode;
        this.desc = desc;
        this.cnDesc = cnDesc;
    }

    private int errorCode;

    private String desc;

    private String cnDesc;


    public static ErrorEnum getInstance(int errorCode){
        for (ErrorEnum value : ErrorEnum.values()) {
            if (value.errorCode == errorCode){
                return value;
            }
        }
        return null;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCnDesc() {
        return cnDesc;
    }

    public void setCnDesc(String cnDesc) {
        this.cnDesc = cnDesc;
    }
}

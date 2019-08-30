package com.example.common.face.constant;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:33
 * Desc: 活体检测控制
 */
public enum  LivenessControlEnum {
    NONE("不进行控制"),
    LOW("较低的活体要求(高通过率 低攻击拒绝率)"),
    NORMAL("一般的活体要求(平衡的攻击拒绝率, 通过率)"),
    HIGH("较高的活体要求");

    LivenessControlEnum(String desc){
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

package com.example.common.face.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/8/30 13:57
 * Desc: 请求百度API接口结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceResult implements Serializable {
    private String logId;

    private String errorMsg;

    private int cached;

    private int errorCode;

    private long timestamp;

    private JSONObject data;

    public boolean isSuccess(){
        return 0 == this.errorCode ? true : false;
    }
}

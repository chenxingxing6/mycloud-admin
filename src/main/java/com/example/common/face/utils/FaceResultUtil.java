package com.example.common.face.utils;

import com.example.common.exception.BizException;
import com.example.common.face.constant.FaceConstant;
import com.example.common.face.dto.FaceResult;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: lanxinghua
 * Date: 2019/8/30 13:52
 * Desc: 结果工具封装
 */
public class FaceResultUtil {
    private static final Logger logger = LoggerFactory.getLogger("百度API接口请求结果解析");

    public static FaceResult isSuccess(JSONObject res){
        FaceResult result = parseJsonObject(res);
        if (!result.isSuccess()){
            throw new BizException("百度接口请求失败");
        }
        return result;
    }


    /**
     * 解析JsonObject
     * @return
     */
    private static FaceResult parseJsonObject(JSONObject res){
        FaceResult faceResult = FaceResult.builder().build();
        try {
            String logId = res.has(FaceConstant.LOG_ID) ? res.getString(FaceConstant.LOG_ID) : "0";
            int errorCode = res.has(FaceConstant.ERROR_CODE) ? res.getInt(FaceConstant.ERROR_CODE) : 0;
            String errorMsg = res.has(FaceConstant.ERROR_MSG) ? res.getString(FaceConstant.ERROR_MSG) : "";
            int cached = res.has(FaceConstant.CACHED) ? res.getInt(FaceConstant.CACHED) : 0;
            long timestamp = res.has(FaceConstant.TIMESTAMP) ? res.getLong(FaceConstant.TIMESTAMP) : 0;
            String dataString = res.has(FaceConstant.RESULT) ? res.getString(FaceConstant.RESULT) : "";
            com.alibaba.fastjson.JSONObject data = com.alibaba.fastjson.JSONObject.parseObject(dataString);

            faceResult.setLogId(logId);
            faceResult.setErrorCode(errorCode);
            faceResult.setErrorMsg(errorMsg);
            faceResult.setCached(cached);
            faceResult.setTimestamp(timestamp);
            faceResult.setData(data);
        }catch (Exception e){
            logger.error("JSONObject解析失败", e);
        }
        return faceResult;
    }
}

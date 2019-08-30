package com.example.common.face.utils;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:04
 * Desc: 人脸识别工具
 */
public class FaceUtil {
    private static final String APP_ID = "15303677";
    private static final String APP_KEY = "OoOqVhksdmc3k6ZRNId8Apjd";
    private static final String SECRET_KEY = "unjW6yGDd2B50KQZfulCWfNAgwA75wLI";

    private static volatile AipFace client = new AipFace(APP_ID, APP_KEY, SECRET_KEY);
    // 创建单例避免多次获取sdk
    public static AipFace getClient(){
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }


    /**
     * 编码
     * @param form
     * @return
     */
    public static String encodeBase64(byte[] form){
        return Base64Util.encode(form);
    }

    /**
     * 解码
     * @param data
     * @return
     */
    public static byte[] decodeBase64(String data){
        return Base64Util.decode(data);
    }
}

package com.example.common.utils;

/**
 * Redis所有Keys
 *
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }

    public static String getShiroSessionKey(String key){
        return "sessionid:" + key;
    }

    public static final String SERVER_INFO_KEY = "disk:server";
    public static final int SERVER_INFO_TIME = 60*60; //一天


    public static final String INDEX_INFO_KEY = "disk:index:";
    public static final int INDEX_INFO_TIME = 60*60; //一天
}

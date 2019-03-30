package com.example.common.utils;

import java.util.HashMap;


/**
 * Map工具类
 *
 * @Author: cxx
 * @Date: 2019/1/1 16:30
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}

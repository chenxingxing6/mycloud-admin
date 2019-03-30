package com.example.common.validator;

import com.example.common.exception.BizException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 * @Author: cxx
 * @Date: 2019/1/1 16:30
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new BizException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new BizException(message);
        }
    }
}

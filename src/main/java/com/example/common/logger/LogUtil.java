package com.example.common.logger;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * User: lanxinghua
 * Date: 2019/8/15 11:35
 * Desc:
 */
public class LogUtil {
    //warn、error级别
    private static final String EXCEPTION_TAG = "Exception: ";
    //异常拆分符号
    private static final String EXCEPTION_SPLIT = " ==> ";

    public static void info(Logger log, Marker marker, String format, Object... arguments) {
        log.info(marker, format, arguments);
    }


}

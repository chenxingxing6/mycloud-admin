package com.example.common.logger;

import org.slf4j.Logger;

/**
 * User: lanxinghua
 * Date: 2019/8/15 11:33
 * Desc: 日志工厂
 */
public class LoggerFactory {
    //超时日志
    public final static Logger TIME_OUT_LOGGER = org.slf4j.LoggerFactory.getLogger("TIME_OUT");

    //异常Handler日志
    public final static Logger EXCEPTION_HANDLER_LOGGER = org.slf4j.LoggerFactory.getLogger("EXCEPTION_HANDLER");

    //业务日志
    public final static Logger BUSINESS_LOGGER = org.slf4j.LoggerFactory.getLogger("BUSINESS");

    //页面拆分
    public final static Logger PAGE_SPLIT_LOGGER = org.slf4j.LoggerFactory.getLogger("PAGE_SPLIT");

}

package com.example.common.logger;

import java.util.Map;

/**
 * User:
 * Date: 2019/8/15 14:40
 * Desc:
 */
public interface IMyLogger {

    /**
     *
     * @param format    待格式文案
     * @param arguments 参数
     */
    void trace(String format, Object... arguments);

    /**
     *
     * @param tag       标签
     * @param format    待格式文案
     * @param arguments 参数
     */
    void traceWithTag(String tag, String format, Object... arguments);


    /**
     * 输出调试信息
     *
     * @param format    待格式文案
     * @param arguments 参数
     */
    void debug(String format, Object... arguments);

    /**
     * 输出调试信息
     *
     * @param tag       标签
     * @param format    待格式文案
     * @param arguments 参数
     */
    void debugWithTag(String tag, String format, Object... arguments);

    /**
     * 输出调试信息
     *
     * @param alert 监控信息
     * @param msg   日志信息
     */
    void debug(Map<String, Object> alert, String msg);

    /**

     /**
     * 输出普通信息
     *
     * @param format    待格式文案
     * @param arguments 参数
     */
    void info(String format, Object... arguments);

    /**
     * 输出普通信息
     *
     * @param tag       标签
     * @param format    待格式文案
     * @param arguments 参数
     */
    void infoWithTag(String tag, String format, Object... arguments);

    /**
     * 输出普通信息
     *
     * @param alert 监控信息
     * @param msg   日志信息
     */
    void info(Map<String, Object> alert, String msg);

    /**
     * 输出警告信息
     *
     * @param format    待格式文案
     * @param arguments 参数
     */
    void warn(String format, Object... arguments);

    /**
     * 输出告警信息
     *
     * @param tag       标签
     * @param format    待格式文案
     * @param arguments 参数
     */
    void warnWithTag(String tag, String format, Object... arguments);

    /**
     * 输出警告信息
     *
     * @param alert 监控信息
     * @param msg   日志信息
     */
    void warn(Map<String, Object> alert, String msg);

   /**
     * 输出警告信息
     *
     * @param alert 监控信息
     * @param e     异常信息
     */
    void warn(Map<String, Object> alert, Throwable e);

    /**
     * 输出错误信息
     *
     * @param format    待格式文案
     * @param arguments 参数
     */
    void error(String format, Object... arguments);

    /**
     * 输出错误信息
     *
     * @param tag       标签
     * @param format    待格式文案
     * @param arguments 参数
     */
    void errorWithTag(String tag, String format, Object... arguments);

    /**
     * 输出错误信息
     *
     * @param msg 日志信息
     * @param e   异常信息
     */
    void error(String msg, Throwable e);

    /**
     * 输出错误信息
     *
     * @param alert 监控信息
     * @param msg   日志信息
     * @param e     异常信息
     */
    void error(Map<String, Object> alert, String msg, Throwable e);

    /**
     * 获取原始的logger
     * @return 原始的logger
     */
    org.slf4j.Logger getOrignalLogger();
}

package com.example.common.logger;

import ch.qos.logback.classic.LoggerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: lanxinghua
 * Date: 2019/8/15 11:33
 * Desc: 日志工厂
 */
public class LoggerFactory {
    private static Map<String, IMyLogger> loggerMap;
    static {
        loggerMap = new ConcurrentHashMap<>();
    }

    // MQ日志
    public static IMyLogger MQ_LOGGER = getLogger(LoggerTagEnum.MQ);

    // 请求日志
    public static IMyLogger ACCESS_LOGGER = getLogger(LoggerTagEnum.ACCESS);

    // 业务异常日志
    public static IMyLogger BIZ_ERROR_LOGGER = getLogger(LoggerTagEnum.BIZ_ERROR);

    // 业务日志
    public static IMyLogger BUSINESS_LOGGER = getLogger(LoggerTagEnum.BUSINESS);

    // 超时日志
    public static IMyLogger TIME_OUT_LOGGER = getLogger(LoggerTagEnum.TIME_OUT);

    // 慢接口日志
    public static IMyLogger TIME_LONG_LOGGER = getLogger(LoggerTagEnum.TIME_LONG);

    // 监控日志
    public static IMyLogger ALERT_MONITOR_LOGGER = getLogger(LoggerTagEnum.ALERT_MONITOR);

    public static IMyLogger getLogger(Class clazz) {
        return getLogger(clazz.getName(), LoggerTagEnum.BUSINESS.getName());
    }

    public static IMyLogger getLogger(String name, String tag) {
        IMyLogger logger = loggerMap.get(name + tag);
        if (logger == null) {
            LoggerContext lc = (LoggerContext) org.slf4j.LoggerFactory.getILoggerFactory();
            org.slf4j.Logger logger_ = lc.getLogger(name);
            logger = new MyCloudLogger(logger_, tag);
            loggerMap.put(name + tag, logger);
        }
        return logger;
    }

    public static IMyLogger getLogger(LoggerTagEnum loggerTagEnum) {
        return getLogger(loggerTagEnum.getCode(), loggerTagEnum.getName());
    }

    public enum LoggerTagEnum {
        MQ("mq", "消息通知"),
        ACCESS("access", "请求日志"),
        BIZ_ERROR("bizError", "业务异常"),
        BUSINESS("business", "业务日志"),
        TIME_OUT("timeOut", "超时日志"),
        TIME_LONG("timeLong", "慢服务"),
        ALERT_MONITOR("alertMonitor", "告警监控");

        private String code;
        private String name;

        LoggerTagEnum(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}

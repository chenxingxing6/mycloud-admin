package com.example.common.logger;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.slf4j.helpers.MessageFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/8/15 15:00
 * Desc:
 */
public class MyCloudLogger implements IMyLogger {
    private Logger logger;
    private String tag;

    // test
    public static void main(String[] args) {
        System.out.println(MessageFormatter.arrayFormat("hello 1:{} 2:{}", Lists.newArrayList(1, 2).toArray()));
    }

    public MyCloudLogger(Logger logger, String tag) {
        this.logger = logger;
        this.tag = tag;
    }

    // tag
    private String appendTag(String tag, String msg) {
        if (msg != null && msg.length() > 0) {
            return "[" + tag + "]。" + msg;
        } else {
            return "[" + tag + "]";
        }
    }

    private String appendTag(String msg) {
        return appendTag(tag, msg);
    }

    // json
    private String appendJson(Map<String, Object> alert, String msg) {
        if (alert == null) {
            alert = new HashMap<>();
        }
        if (msg != null && msg.length() > 0) {
            alert.put("otherMsg", msg);
        }
        return appendTag("json:" + JSON.toJSONString(alert));
    }


    // 提取公共方法
    private void print(Level level, String tag, String format, Object... arguments){
        try {
            switch (level){
                case TRACE:{
                    if (arguments == null || arguments.length == 0){
                        if (tag == null){
                            logger.trace(appendTag(format));
                        }else {
                            logger.trace(appendTag(tag, format));
                        }
                    }else {
                        if (tag == null){
                            logger.trace(appendTag(MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }else {
                            logger.trace(appendTag(tag, MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }
                    }
                    break;
                }
                case DEBUG:{
                    if (arguments == null || arguments.length == 0){
                        if (tag == null){
                            logger.debug(appendTag(format));
                        }else {
                            logger.debug(appendTag(tag, format));
                        }
                    }else {
                        if (tag == null){
                            logger.debug(appendTag(MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }else {
                            logger.debug(appendTag(tag, MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }
                    }
                    break;
                }
                case INFO:{
                    if (arguments == null || arguments.length == 0){
                        if (tag == null){
                            logger.info(appendTag(format));
                        }else {
                            logger.info(appendTag(tag, format));
                        }
                    }else {
                        if (tag == null){
                            logger.info(appendTag(MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }else {
                            logger.info(appendTag(tag, MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }
                    }
                    break;
                }
                case WARN:{
                    if (arguments == null || arguments.length == 0){
                        if (tag == null){
                            logger.warn(appendTag(format));
                        }else {
                            logger.warn(appendTag(tag, format));
                        }
                    }else {
                        if (tag == null){
                            logger.warn(appendTag(MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }else {
                            logger.warn(appendTag(tag, MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }
                    }
                    break;
                }
                case ERROR:{
                    if (arguments == null || arguments.length == 0){
                        if (tag == null){
                            logger.error(appendTag(format));
                        }else {
                            logger.error(appendTag(tag, format));
                        }
                    }else {
                        if (tag == null){
                            logger.error(appendTag(MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }else {
                            logger.error(appendTag(tag, MessageFormatter.arrayFormat(format, arguments).getMessage()));
                        }
                    }
                    break;
                }
                default:{}
            }
        }catch (Throwable ignored){ }
    }

    private void printWithMap(Level level, Map<String, Object> alert, String msg, Throwable e){
        try {
            switch (level){
                case TRACE:{
                    logger.trace(appendJson(alert, msg));
                    break;
                }
                case DEBUG:{
                    logger.debug(appendJson(alert, msg));
                    break;
                }
                case INFO:{
                    logger.info(appendJson(alert, msg));
                    break;
                }
                case WARN:{
                    if (e == null){
                        logger.warn(appendJson(alert, msg));
                    }else {
                        logger.warn(appendJson(alert, msg), e);
                    }
                    break;
                }
                case ERROR:{
                    if (e == null) {
                        logger.error(appendJson(alert, msg));
                    }else {
                        logger.error(appendJson(alert, msg), e);
                    }
                    break;
                }
                default:{}
            }
        }catch (Throwable ignored){ }
    }



    @Override
    public void trace(String format, Object... arguments) {
        print(Level.TRACE, null, format, arguments);
    }

    @Override
    public void traceWithTag(String tag, String format, Object... arguments) {
        print(Level.TRACE, tag, format, arguments);
    }

    @Override
    public void debug(String format, Object... arguments) {
        print(Level.DEBUG, null, format, arguments);
    }

    @Override
    public void debugWithTag(String tag, String format, Object... arguments) {
        print(Level.DEBUG, tag, format, arguments);
    }

    @Override
    public void debug(Map<String, Object> alert, String msg) {
        printWithMap(Level.DEBUG, alert, msg, null);
    }

    @Override
    public void info(String format, Object... arguments) {
        print(Level.INFO, null, format, arguments);
    }

    @Override
    public void infoWithTag(String tag, String format, Object... arguments) {
        print(Level.INFO, tag, format, arguments);
    }

    @Override
    public void info(Map<String, Object> alert, String msg) {
        printWithMap(Level.INFO, alert, msg, null);
    }

    @Override
    public void warn(String format, Object... arguments) {
        print(Level.WARN, null, format, arguments);
    }

    @Override
    public void warnWithTag(String tag, String format, Object... arguments) {
        print(Level.WARN, tag, format, arguments);
    }

    @Override
    public void warn(Map<String, Object> alert, String msg) {
        printWithMap(Level.WARN, alert, msg, null);
    }

    @Override
    public void warn(Map<String, Object> alert, Throwable e) {
        printWithMap(Level.WARN, alert, null, e);
    }

    @Override
    public void error(String format, Object... arguments) {
        print(Level.ERROR, null, format, arguments);
    }

    @Override
    public void errorWithTag(String tag, String format, Object... arguments) {
        print(Level.ERROR, tag, format, arguments);
    }

    @Override
    public void error(String msg, Throwable e) {
        printWithMap(Level.ERROR, null, null, e);
    }

    @Override
    public void error(Map<String, Object> alert, String msg, Throwable e) {
        printWithMap(Level.ERROR, alert, msg, e);
    }

    @Override
    public Logger getOrignalLogger() {
        return logger;
    }
}

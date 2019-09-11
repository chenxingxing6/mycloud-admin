package com.example.common.event;

import org.springframework.context.ApplicationEvent;

/**
 * User: lanxinghua
 * Date: 2019/9/11 21:16
 * Desc:
 */
public class MyEvent<T> extends ApplicationEvent {
    private T msg;

    public MyEvent(T source) {
        super(source);
        this.msg = source;
    }

    public T getMsg() {
        return msg;
    }
}

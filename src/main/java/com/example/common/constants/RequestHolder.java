package com.example.common.constants;


import javax.servlet.http.HttpServletRequest;

/**
 * User: lanxinghua
 * Date: 2019/9/4 16:28
 */
public class RequestHolder {
    private final static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        return threadLocal.get();
    }

    public static void setRequest(HttpServletRequest request){
        threadLocal.set(request);
    }

    public static void clear(){
        threadLocal.set(null);
    }
}

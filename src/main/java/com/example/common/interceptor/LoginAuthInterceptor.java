package com.example.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.example.common.constants.RequestHolder;
import com.example.common.constants.SessionHolder;
import com.example.modules.sys.dao.SysUserDao;
import com.example.modules.sys.entity.SysUserEntity;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: lanxinghua
 * Date: 2019/9/4 16:33
 * Desc: 登陆拦截器，将用户信息保存在LoginSession中
 */
@Component
public class LoginAuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginAuthInterceptor.class);
    @Resource
    private SysUserDao sysUserDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // session获取用户信息
        SysUserEntity user = new SysUserEntity();
        user.setUsername("用户名");
        SessionHolder.setSsoSession(user);
        logger.info("请求方法前拦截" + Thread.currentThread().getName() + JSON.toJSONString(SessionHolder.getSsoSession()));
        return true;
    }
}

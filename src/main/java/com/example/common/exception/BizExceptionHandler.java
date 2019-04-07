package com.example.common.exception;

import com.example.common.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * User: lanxinghua
 * Date: 2019/3/17 16:41
 * Desc: 异常处理器
 */
@RestControllerAdvice
public class BizExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BizException.class)
    public R handleBizException(BizException e){
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMsg());
        return r;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e){
        logger.error(e.getMessage(), e);
        return R.error("数据库中已存在该记录");
    }

    @ExceptionHandler(AuthorizationException.class)
    public R handleAuthorizationException(AuthorizationException e){
        logger.error(e.getMessage(), e);
        return R.error("没有权限，请联系管理员授权");
    }

    @ExceptionHandler(UnauthorizedException.class)
    public R handleUnauthorizedException(UnauthorizedException e){
        logger.error(e.getMessage(), e);
        return R.error("没有权限，请联系管理员授权");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        logger.error(e.getMessage(), e);
        return R.error();
    }

}

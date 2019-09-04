package com.example.common.constants;

import com.example.modules.sys.entity.SysUserEntity;

/**
 * User: lanxinghua
 * Date: 2019/9/4 17:28
 * Desc:
 */
public class SessionHolder {

    private static final ThreadLocal<SysUserEntity> threadLocal = new ThreadLocal<SysUserEntity>();

    /**
     * 得到session
     * @return
     */
    public static SysUserEntity getSsoSession() {
        return threadLocal.get();
    }

    /**
     * 将session与当前线程绑定
     */
    public static void setSsoSession(final SysUserEntity sysUserEntity) {
        threadLocal.set(sysUserEntity);
    }

    public static void clear() {
        threadLocal.set(null);
    }

}

package com.example.transactional;

import com.example.BaseTest;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysConfigService;
import com.example.modules.sys.service.ISysUserService;
import com.google.common.util.concurrent.UncheckedTimeoutException;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/6/28 17:10
 * Desc: 事务测试
 */
public class TransactionalTest extends BaseTest {
    @Resource
    private ISysUserService sysUserService;


    public void a(){
        SysUserEntity user = sysUserService.selectById(1);
        user.setStatus(0);
        sysUserService.update(user);
        System.out.println("");
    }

    public void b(){
        SysUserEntity user = sysUserService.selectById(2);
        user.setStatus(0);
        sysUserService.update(user);
    }
}

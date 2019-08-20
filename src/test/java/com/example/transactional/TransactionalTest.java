package com.example.transactional;

import com.example.BaseTest;
import com.example.modules.sys.entity.SysConfigEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysConfigService;
import com.example.modules.sys.service.ISysUserService;
import com.google.common.util.concurrent.UncheckedTimeoutException;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/6/28 17:10
 * Desc: 事务测试
 */
public class TransactionalTest extends BaseTest {
    @Resource
    private ISysUserService sysUserService;


    /**
     *  1.都会回滚
     *  @Transactional(rollbackFor = Exception.class)
     * 	@Override
     * 	public void rollbackTest() {
     * 		SysUserEntity user = sysUserService.selectById(1);
     * 		user.setStatus(0);
     * 		sysUserService.update(user);
     * 		System.out.println("a ok ......");
     * 		b();
     * 	}
     *
     * 	private void b(){
     * 		SysUserEntity user = sysUserService.selectById(2);
     * 		user.setStatus(0);
     * 		sysUserService.update(user);
     * 		System.out.println("b ok ......");
     * 		int i = 1/0;
     * 	}
     */
    @Test
    public void test01(){
        sysUserService.rollbackTest();
    }


    /**
     *  2.都不回滚
     *  @Override
     * 	public void rollbackTest() {
     * 		SysUserEntity user = sysUserService.selectById(1);
     * 		user.setStatus(0);
     * 		sysUserService.update(user);
     * 		System.out.println("a ok ......");
     * 		b();
     * 	}
     *
     * 	@Transactional(rollbackFor = Exception.class)
     * 	protected void b(){
     * 		SysUserEntity user = sysUserService.selectById(2);
     * 		user.setStatus(0);
     * 		sysUserService.update(user);
     * 		System.out.println("b ok ......");
     * 		int i = 1/0;
     * 	}
     */
    @Test
    public void test02(){
        sysUserService.rollbackTest();
    }

    /**
     *  3.b方法回滚
     *  @Override
     * 	public void rollbackTest() {
     * 		SysUserEntity user = sysUserService.selectById(1);
     * 		user.setStatus(0);
     * 		sysUserService.update(user);
     * 		System.out.println("a ok ......");
     * 		SysUserServiceImpl sysUserService = new SysUserServiceImpl();
     * 		sysUserService.b();
     * 	}
     *
     * 	@Transactional(rollbackFor = Exception.class)
     * 	protected void b(){
     * 		SysUserEntity user = sysUserService.selectById(2);
     * 		user.setStatus(0);
     * 		sysUserService.update(user);
     * 		System.out.println("b ok ......");
     * 		int i = 1/0;
     * 	}
     */
    @Test
    public void test03(){
        sysUserService.rollbackTest();
    }
}

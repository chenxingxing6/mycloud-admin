package com.example.transactional;

import com.alibaba.fastjson.JSON;
import com.example.BaseTest;
import com.example.modules.sys.entity.SysConfigEntity;
import com.example.modules.sys.service.ISysConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/8/20 15:45
 * Desc: 默认每条测试方法都是完成后回滚[AbstractTransactionalJUnit4SpringContextTests]
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RollbackTest1 extends AbstractTransactionalJUnit4SpringContextTests {
    @Resource
    private ISysConfigService configService;

    @Test
    public void test01(){
        SysConfigEntity configEntity = new SysConfigEntity();
        Long id = 11L;
        configEntity.setId(id);
        configEntity.setParamKey("mykey");
        configEntity.setParamValue("myvalue");
        configEntity.setRemark("myremark");
        configService.save(configEntity);
        SysConfigEntity entity = configService.selectById(id);
        System.out.println(JSON.toJSONString(entity));
    }
}

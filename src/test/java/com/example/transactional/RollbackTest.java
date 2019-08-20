package com.example.transactional;

import com.alibaba.fastjson.JSON;
import com.example.BaseTest;
import com.example.modules.sys.entity.SysConfigEntity;
import com.example.modules.sys.service.ISysConfigService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/8/20 15:45
 * Desc: 单元测试的时候频繁操作数据库需要修改很多数据,造成不必要的操作,添加事务之后就可以重复对一条数据进行操作,并且在返回结果后进行回滚.
 */
public class RollbackTest extends BaseTest {
    @Resource
    private ISysConfigService configService;

    @Transactional
    @Rollback
    @Test
    public void test01(){
        SysConfigEntity configEntity = new SysConfigEntity();
        Long id = 11L;
        configEntity.setId(id);
        configEntity.setParamKey("mykey");
        configEntity.setParamValue("myvalue");
        configEntity.setRemark("myremark");
        configService.save(configEntity);
        BUSINESS_LOGGER.info("插入成功。。。");
        SysConfigEntity entity = configService.selectById(id);
        System.out.println(JSON.toJSONString(entity));

    }
}

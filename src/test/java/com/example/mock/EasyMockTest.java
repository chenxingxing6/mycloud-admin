package com.example.mock;

import com.alibaba.common.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.example.modules.sys.dao.SysConfigDao;
import com.example.modules.sys.dao.SysLogDao;
import com.example.modules.sys.entity.SysConfigEntity;
import com.example.modules.sys.entity.SysLogEntity;
import com.example.modules.sys.service.ISysConfigService;
import com.example.modules.sys.service.ISysLogService;
import com.example.modules.sys.service.impl.SysConfigServiceImpl;
import com.example.modules.sys.service.impl.SysLogServiceImpl;
import org.easymock.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.http.HttpServletRequest;

/**
 * User: lanxinghua
 * Date: 2019/8/16 10:53
 * Desc: 步骤
 * 1.生成mock接口
 * 2.设置预期行为
 * 3.将mock对象切换到replay状态
 * 4.测试
 * 5.验证mock对象行为
 */
@RunWith(EasyMockRunner.class)
public class EasyMockTest extends EasyMockSupport {

    @TestSubject
    private ISysConfigService configService = new SysConfigServiceImpl();

    @Mock
    private SysConfigDao configDao;

    private HttpServletRequest servletRequest = createMockHttpServeletRequest();

    /**
     * 创建 MockHttpServletRequest
     * @return
     */
    private MockHttpServletRequest createMockHttpServeletRequest(){
        MockServletContext context = new MockServletContext();
        return new MockHttpServletRequest(context);
    }

    private SysConfigEntity entity = null;
    private static final String id = "10000";

    @Before
    public void init(){
        entity = new SysConfigEntity();
        entity.setId(Convert.asLong(id));
        entity.setRemark("remark");
        entity.setParamKey("key");
        entity.setParamValue("value");
    }

    @Test
    public void test01(){
        // 1.生成mock接口,如果需要mock多个接口，用controllerMock来管理
        // IMocksControl control = EasyMock.createControl();
        // SysConfigDao configDao = control.createMock(SysConfigDao.class);
        // SysLogDao logDao = control.createMock(SysLogDao.class);

        // 2.设置预期行为
        EasyMock.expect(configDao.selectById(id)).andReturn(entity);

        // 3.将mock切换到replay状态
        replayAll();

        // 4.测试
        SysConfigEntity result = configService.selectById(id);

        // 5.验证mock对象行为
        // EasyMock.verify(configDao);
        verifyAll();

        System.out.println(JSON.toJSONString(result));
        Assert.assertNotNull(result);
    }

    @Test
    public void test02(){
        SysLogEntity entity = Mockito.mock(SysLogEntity.class);
        System.out.println(JSON.toJSONString(entity));
    }

}

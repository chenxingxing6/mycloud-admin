package com.example.mock;

import com.example.modules.sys.service.ISysConfigService;
import com.example.modules.sys.service.ISysLogService;
import com.example.modules.sys.service.impl.SysLogServiceImpl;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import javax.servlet.http.HttpServletRequest;

/**
 * User: lanxinghua
 * Date: 2019/8/16 10:53
 * Desc:
 */
@RunWith(EasyMockRunner.class)
public class EasyMockTest {

    @TestSubject
    private ISysLogService logService = new SysLogServiceImpl();

    @Mock
    private ISysConfigService configService;

    private HttpServletRequest servletRequest = createMockHttpServeletRequest();

    /**
     * 创建 MockHttpServletRequest
     * @return
     */
    private MockHttpServletRequest createMockHttpServeletRequest(){
        MockServletContext context = new MockServletContext();
        return new MockHttpServletRequest(context);
    }
}

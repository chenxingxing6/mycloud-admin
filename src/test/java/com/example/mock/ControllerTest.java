package com.example.mock;

import com.example.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * User: lanxinghua
 * Date: 2019/8/16 11:00
 * Desc: MockMvc对系统Controller进行测试，对数据库增删改查结束后会还原数据库
 * spring-test
 */
public class ControllerTest extends BaseTest {
    @Resource
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    // 初始化MockMvc对象
    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void test01() throws Exception {
        String responseString = this.mockMvc.perform(
                get("http://www.baidu.com").param("name", "lxh")
        ).andReturn().getResponse().getContentAsString();

        System.out.println(responseString);
    }

    // get测试
    @Test
    public void test02() throws Exception {
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders
                .get("http://www.baidu.com")
                .param("key", "value")
                .header("key", "value")
        ).andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("html"))
        .andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getStatus());
        System.out.println(response.getContentAsString());
    }


    // post测试
    @Test
    public void test03() throws Exception {
        String responseString = this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("http://www.baidu.com")       // 请求路径
                        .param("key", "value")          // 参数
                        .header("key", "value")         // 请求头
                        .content("请求体")                              // 请求体
                        .contentType(MediaType.APPLICATION_JSON_UTF8)  // content-type
                        .characterEncoding("UTF-8")                    // 请求编码
        ).andDo(MockMvcResultHandlers.print())                         // 打印请求详细信息
                .andExpect(MockMvcResultMatchers.status().isOk())      // 判断返回状态
                .andExpect(MockMvcResultMatchers.content().string("html"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }
}

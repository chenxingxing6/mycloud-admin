package com.example;

import com.example.common.utils.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/4/6 13:28
 * Desc: Redis服务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Resource
    private RedisUtils redisUtils;

    @Test
    public void test1(){
        redisUtils.set("key", "value");
        System.out.println(redisUtils.get("key"));
    }
}

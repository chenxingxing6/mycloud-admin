package com.example;

import com.example.common.logger.LogUtil;
import com.example.common.logger.LoggerFactory;
import com.example.common.logger.LoggerMarkers;
import com.example.common.utils.IdGen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {
	@Resource
	IdGen idGen;

	@Value("${mycloud.env}")
	private String env;

	@Test
	public void contextLoads() {
		System.out.println(idGen.nextId());
		System.out.println(env);
	}

	@Test
	public void logTest() throws Exception{
		for (int i = 0; i <10000 ; i++) {
			LogUtil.info(LoggerFactory.PAGE_SPLIT_LOGGER, LoggerMarkers.BUSINESS,"msg:{}","hello world");
			TimeUnit.SECONDS.sleep(1);
		}
	}
}


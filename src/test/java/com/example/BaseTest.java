package com.example;

import com.example.common.logger.IMyLogger;
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
	private static final IMyLogger logger = LoggerFactory.getLogger(LoggerFactory.class);
	// MQ日志
	public static IMyLogger MQ_LOGGER = LoggerFactory.MQ_LOGGER;
	// 请求日志
	public static IMyLogger ACCESS_LOGGER = LoggerFactory.ACCESS_LOGGER;
	// 业务异常日志
	public static IMyLogger BIZ_ERROR_LOGGER = LoggerFactory.BIZ_ERROR_LOGGER;
	// 业务日志
	public static IMyLogger BUSINESS_LOGGER = LoggerFactory.BUSINESS_LOGGER;
	// 超时日志
	public static IMyLogger TIME_OUT_LOGGER = LoggerFactory.TIME_OUT_LOGGER;
	// 慢接口日志
	public static IMyLogger TIME_LONG_LOGGER = LoggerFactory.TIME_LONG_LOGGER;
	// 监控日志
	public static IMyLogger ALERT_MONITOR_LOGGER = LoggerFactory.ALERT_MONITOR_LOGGER;


	@Resource
	IdGen idGen;

	@Value("${mycloud.env}")
	private String env;

	@Test
	public void contextLoads() {
		System.out.println(idGen.nextId());
		System.out.println(env);
		System.out.println(LoggerFactory.class);
		for (int i = 0; i < 100; i++) {
			logger.warn("msg:{}", i);
		}
	}

	@Test
	public void logTest() throws Exception{
		for (int i = 0; i <10000 ; i++) {
			int num = i % 7;
			switch (num){
				case 0:{
					MQ_LOGGER.info("msg:{}", "mq消费成功" + i);
					break;
				}
				case 1:{
					ACCESS_LOGGER.info("msg:{}", "请求日志" + i);
					break;
				}
				case 2:{
					BIZ_ERROR_LOGGER.error("操作失败", new Exception("error ..."));
					break;
				}
				case 3:{
					BUSINESS_LOGGER.info("msg:{}", "业务日志" + i);
					break;
				}
				case 4:{
					TIME_OUT_LOGGER.warn("msg:{}", "超时日志" + i);
					break;
				}
				case 5:{
					TIME_LONG_LOGGER.warn("msg:{}", "慢接口日志" + i);
					break;
				}
				case 6:{
					ALERT_MONITOR_LOGGER.info("msg:{}", "监控日志" + i);
					break;
				}
			}
			TimeUnit.MILLISECONDS.sleep(100);
		}
	}
}


package com.example.modules.job.task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 *
 * testTask为spring bean的名称
 */
@Component("testTask")
public class TestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void test1(String params){
		logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
	}

	public void test2(){
		logger.info("我是不带参数的test2方法，正在被执行");
	}
}

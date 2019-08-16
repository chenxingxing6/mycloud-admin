package com.example;

import com.example.common.logger.LogUtil;
import com.example.common.logger.LoggerMarkers;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.modules.*.dao"})
@EnableSwagger2
public class MycloudAdminApplication extends SpringBootServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(MycloudAdminApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MycloudAdminApplication.class, args);
		LogUtil.debug(logger, LoggerMarkers.BUSINESS, "启动成功....");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MycloudAdminApplication.class);
	}
}


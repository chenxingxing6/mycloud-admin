package com.example;

import org.mybatis.spring.annotation.MapperScan;
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
	@Value("${mycloud.env}")
	private static String env;

	public static void main(String[] args) {
		SpringApplication.run(MycloudAdminApplication.class, args);
		System.out.println("启动成功。。。" + env);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MycloudAdminApplication.class);
	}
}


package com.meiyou.hbase.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@AutoConfigurationPackage
@SpringBootApplication
@ComponentScan({"com.meiyou.hbase.manager", "com.meiyou.shiro"})
@EnableJpaRepositories(basePackages = {"com.meiyou.hbase.manager", "com.meiyou.shiro"})
@EntityScan(basePackages = {"com.meiyou.hbase.manager", "com.meiyou.shiro"})
@EnableScheduling // 定时器总开关
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

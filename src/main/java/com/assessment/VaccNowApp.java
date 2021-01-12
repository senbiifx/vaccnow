package com.assessment;

import com.assessment.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class VaccNowApp {

	public static void main(String[] args) {
		SpringApplication.run(VaccNowApp.class, args);
	}

}

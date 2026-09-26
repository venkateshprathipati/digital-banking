package com.novalabs.digitalbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableCaching
@EnableScheduling
@EnableRetry
public class DigitalBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingApplication.class, args);
	}

}

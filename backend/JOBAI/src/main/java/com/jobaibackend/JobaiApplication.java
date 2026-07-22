package com.jobaibackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobaiApplication {

	 private static final Logger logger =
            LoggerFactory.getLogger(JobaiApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(JobaiApplication.class, args);

		logger.info("========================================");
        logger.info("JobAI Backend started successfully.");
        logger.info("Version : 0.1.0");
        logger.info("Port    : 8080");
        logger.info("========================================");
	}

}

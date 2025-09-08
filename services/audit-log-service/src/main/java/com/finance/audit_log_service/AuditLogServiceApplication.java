package com.finance.audit_log_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuditLogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditLogServiceApplication.class, args);
	}

}

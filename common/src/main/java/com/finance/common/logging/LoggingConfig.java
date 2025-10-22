package com.finance.common.logging;

import java.util.UUID;

import org.slf4j.MDC;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingConfig {

    public static void startRequest(String path, String service) {
        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("service", service);
        MDC.put("path", path);
    }

    public static void endRequest() {
        MDC.clear();
    }
}
package com.finance.common.logging;

import java.util.UUID;

import org.slf4j.MDC;

public final class LoggingConfig {

    private LoggingConfig() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void startRequest(String path, String service) {
        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("service", service);
        MDC.put("path", path);
    }

    public static void endRequest() {
        MDC.clear();
    }
}

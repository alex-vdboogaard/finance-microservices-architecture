package com.finance.notification_service.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long id,
    Long userId,
    String title,
    String description,
    LocalDateTime timestamp
) {}

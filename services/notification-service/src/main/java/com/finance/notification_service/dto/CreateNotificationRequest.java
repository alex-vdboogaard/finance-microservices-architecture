package com.finance.notification_service.dto;

import jakarta.validation.constraints.NotNull;

public record CreateNotificationRequest(
    Long userId,
    @NotNull String title,
    @NotNull String description
) {}

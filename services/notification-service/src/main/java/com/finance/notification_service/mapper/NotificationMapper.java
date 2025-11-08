package com.finance.notification_service.mapper;

import com.finance.notification_service.dto.NotificationResponse;
import com.finance.notification_service.model.Notification;

public final class NotificationMapper {
    private NotificationMapper(){}

    public static NotificationResponse toResponse(Notification entity) {
        return new NotificationResponse(
            entity.getId(),
            entity.getUserId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getTimestamp()
        );
    }
}

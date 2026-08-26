package com.finance.notification_service.dto.event;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransferNotificationEvent(
        Long fromAccountId,
        Long toAccountId,
        String description) implements Serializable {

    public TransferNotificationEvent(Long fromAccountId, String description) {
        this(fromAccountId, null, description);
    }
}

package com.finance.notification_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finance.common.dto.ApiResponse;
import com.finance.common.logging.LoggingConfig;
import com.finance.notification_service.dto.CreateNotificationRequest;
import com.finance.notification_service.dto.NotificationResponse;
import com.finance.notification_service.model.Notification;
import com.finance.notification_service.service.NotificationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "Endpoints for retrieving and creating notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications() {
        ApiResponse<List<Notification>> response = ApiResponse.<List<Notification>>builder()
                .meta(ApiResponse.Meta.builder().message("Fetched notifications successfully").build())
                .data(notificationService.getAll())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<Notification>>> getByUser(@RequestParam Long userId) {
        ApiResponse<List<Notification>> response = ApiResponse.<List<Notification>>builder()
                .meta(ApiResponse.Meta.builder().message("Fetched notifications successfully").build())
                .data(notificationService.findByUserId(userId))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @Valid @RequestBody CreateNotificationRequest request,
            HttpServletRequest httpRequest) {
        LoggingConfig.startRequest(httpRequest.getRequestURI(), "notification-service");

        log.info("Received request to create notification: userId={}, title={}, description={}", request.userId(), request.title(), request.description());
        try {
            NotificationResponse created = notificationService.createNotification(request);
            ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                    .meta(ApiResponse.Meta.builder().message("Created notification successfully").build())
                    .data(created)
                    .build();
            log.info("Notification created successfully, id={}, userId={}, title={}, timestamp={}", created.id(),
                    created.userId(), created.title(), created.timestamp());
            LoggingConfig.endRequest();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating notification: {}", e.getMessage());
            LoggingConfig.endRequest();
            return ResponseEntity.internalServerError().build();
        }
    }
}

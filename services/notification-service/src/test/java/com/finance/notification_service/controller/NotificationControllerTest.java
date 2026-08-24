package com.finance.notification_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.notification_service.dto.CreateNotificationRequest;
import com.finance.notification_service.dto.NotificationResponse;
import com.finance.notification_service.model.Notification;
import com.finance.notification_service.service.NotificationService;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    @DisplayName("GET /api/v1/notifications should return list of all notifications")
    void shouldGetAllNotifications() throws Exception {
        // Given
        Notification n = Notification.builder()
                .id(1L)
                .userId(10L)
                .title("Alert")
                .description("Sample Alert")
                .build();
        when(notificationService.getAll()).thenReturn(List.of(n));

        // When & Then
        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.message").value("Fetched notifications successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].userId").value(10))
                .andExpect(jsonPath("$.data[0].title").value("Alert"));

        verify(notificationService).getAll();
    }

    @Test
    @DisplayName("GET /api/v1/notifications should return empty array when no notifications exist")
    void shouldReturnEmptyListWhenNoNotifications() throws Exception {
        // Given
        when(notificationService.getAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.message").value("Fetched notifications successfully"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(notificationService).getAll();
    }

    @Test
    @DisplayName("GET /api/v1/notifications/user should return notifications for user")
    void shouldGetNotificationsByUserId() throws Exception {
        // Given
        Long userId = 42L;
        Notification n = Notification.builder()
                .id(1L)
                .userId(userId)
                .title("User Alert")
                .description("Alert details")
                .build();
        when(notificationService.findByUserId(userId)).thenReturn(List.of(n));

        // When & Then
        mockMvc.perform(get("/api/v1/notifications/user").param("userId", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.message").value("Fetched notifications successfully"))
                .andExpect(jsonPath("$.data[0].userId").value(42))
                .andExpect(jsonPath("$.data[0].title").value("User Alert"));

        verify(notificationService).findByUserId(userId);
    }

    @Test
    @DisplayName("GET /api/v1/notifications/user should return 400 when userId query param is missing")
    void shouldReturnBadRequestWhenUserIdParamMissing() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/notifications/user"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/notifications should create notification and return 200")
    void shouldCreateNotification() throws Exception {
        // Given
        CreateNotificationRequest request = new CreateNotificationRequest(10L, "New Alert", "Alert Desc");
        NotificationResponse response = new NotificationResponse(
                1L, 10L, "New Alert", "Alert Desc", LocalDateTime.now()
        );
        when(notificationService.createNotification(any(CreateNotificationRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.message").value("Created notification successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.userId").value(10))
                .andExpect(jsonPath("$.data.title").value("New Alert"));

        verify(notificationService).createNotification(any(CreateNotificationRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/notifications should return 500 when service throws exception")
    void shouldReturn500WhenServiceFails() throws Exception {
        // Given
        CreateNotificationRequest request = new CreateNotificationRequest(10L, "Failed Alert", "Desc");
        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("POST /api/v1/notifications should return 400 when request body is missing")
    void shouldReturnBadRequestWhenRequestBodyMissing() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}

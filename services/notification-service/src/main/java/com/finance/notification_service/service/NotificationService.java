package com.finance.notification_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.notification_service.dto.CreateNotificationRequest;
import com.finance.notification_service.dto.NotificationResponse;
import com.finance.notification_service.mapper.NotificationMapper;
import com.finance.notification_service.model.Notification;
import com.finance.notification_service.repository.NotificationRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    public List<Notification> getAll() {
        return repository.findAll();
    }

    public List<Notification> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification entity = new Notification();
        entity.setUserId(request.userId());
        entity.setTitle(request.title());
        entity.setDescription(request.description());
        Notification saved = repository.save(entity);
        return NotificationMapper.toResponse(saved);
    }
}

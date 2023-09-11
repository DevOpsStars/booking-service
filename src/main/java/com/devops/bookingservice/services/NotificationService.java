package com.devops.bookingservice.services;

import com.devops.bookingservice.model.Notification;
import com.devops.bookingservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    public Notification findOne(String id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> findByReceiverId(Integer receiverId){
        return notificationRepository.findByReceiverId(receiverId);
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }
}

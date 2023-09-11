package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByReceiverId(Integer receiverId);
}

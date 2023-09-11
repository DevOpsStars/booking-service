package com.devops.bookingservice.controller;

import com.devops.bookingservice.model.Notification;
import com.devops.bookingservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = "")
    public ResponseEntity<List<Notification>> getAll() {
        List<Notification> result = notificationService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping(value = "/{receiverId}")
    public ResponseEntity<List<Notification>> getByReceiverId(@PathVariable Integer receiverId) {
        List<Notification> result = notificationService.findByReceiverId(receiverId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

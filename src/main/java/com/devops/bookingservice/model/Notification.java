package com.devops.bookingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
@ToString
public class Notification {
    @Id
    private String id;
    private Integer senderId;
    private NotificationStatus status;
    private String message;
    private Integer receiverId;
}

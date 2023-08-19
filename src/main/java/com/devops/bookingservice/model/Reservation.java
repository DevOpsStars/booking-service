package com.devops.bookingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
public class Reservation {
    @Id
    private String id;
    private Integer userId;
    private Integer lodgeId;
    private Date reservationStart;
    private Date reservationEnd;
    private Integer guestNumber;
    private Double totalPrice;
    private Boolean canceled;
}

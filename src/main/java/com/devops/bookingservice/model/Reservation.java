package com.devops.bookingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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
    private LocalDate reservationStart;
    private LocalDate reservationEnd;
    private Integer guestNumber;
    private Double totalPrice;
    private Boolean canceled;
}

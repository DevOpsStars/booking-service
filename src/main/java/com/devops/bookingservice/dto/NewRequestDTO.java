package com.devops.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewRequestDTO {
    private Integer userId;
    private Integer lodgeId;
    private Date reservationStart;
    private Date reservationEnd;
    private Integer guestNumber;
    private Double totalPrice;
}

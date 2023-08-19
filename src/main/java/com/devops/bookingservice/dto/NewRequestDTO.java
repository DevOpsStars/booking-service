package com.devops.bookingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewRequestDTO {
    private Integer userId;
    private Integer lodgeId;
    private LocalDate reservationStart;
    private LocalDate reservationEnd;
    private Integer guestNumber;
    private Double totalPrice;
}

package com.devops.bookingservice.services;

import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservationFromRequest(ReservationRequest request) {
        Reservation reservation = Reservation.builder()
                .lodgeId(request.getLodgeId())
                .userId(request.getUserId())
                .reservationStart(request.getReservationStart())
                .reservationEnd(request.getReservationEnd())
                .guestNumber(request.getGuestNumber())
                .canceled(false)
                .totalPrice(request.getTotalPrice())
                .build();
        return reservationRepository.save(reservation);
    }
}

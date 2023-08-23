package com.devops.bookingservice.services;

import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public Reservation cancel(String id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation == null)
            return null;
        if (reservation.getReservationStart().isAfter(LocalDate.now().minusDays(1)))
            return null;
        reservation.setCanceled(true);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Integer getCancelCountForUser(Integer userId) {
        return reservationRepository.countIsCanceledByUserId(userId);
    }
}

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
        if (!reservation.getReservationStart().isAfter(LocalDate.now().plusDays(1)))
            return null;
        if (reservation.getCanceled())
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

    public List<Reservation> getAllByUserIdActive(Integer userId) {
        return reservationRepository.findAllByUserIdAndReservationEndAfterAndCanceledIsFalse(userId, LocalDate.now());
    }

    public List<Reservation> getAllByUserId(Integer userId) {
        return reservationRepository.findAllByUserIdAndCanceledIsFalse(userId);
    }

    public List<Reservation> getAllByLodgeIdActive(Integer lodgeId) {
        return reservationRepository.findAllByLodgeIdAndReservationEndAfterAndCanceledIsFalse(lodgeId, LocalDate.now());
    }

    public List<Reservation> getAllByLodgeIdsActive(List<Integer> lodgeIds) {
        return reservationRepository.findAllByLodgeIdInAndReservationEndAfterAndCanceledIsFalse(lodgeIds, LocalDate.now());
    }

    public List<Reservation> getReservationsByPeriod(LocalDate start, LocalDate end, Integer lodgeId) {
        return reservationRepository.findInPeriodByLodge(start, end, lodgeId);
    }

    public Integer getCountReservationsByPeriod(LocalDate start, LocalDate end, Integer lodgeId) {
        return reservationRepository.countInPeriodByLodge(start, end, lodgeId);
    }
}

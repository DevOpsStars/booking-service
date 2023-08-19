package com.devops.bookingservice.services;

import com.devops.bookingservice.dto.NewRequestDTO;
import com.devops.bookingservice.model.RequestStatus;
import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.repository.ReservationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationRequestService {

    private ReservationRequestRepository requestRepository;

    @Autowired
    public ReservationRequestService(ReservationRequestRepository requestRepository, ReservationService reservationService){
        this.requestRepository = requestRepository;
    }

    public ReservationRequest createNew(NewRequestDTO request) {
        // check if already reserved
        if (this.requestRepository
                .findAcceptedByPeriod(request.getReservationStart(), request.getReservationEnd())
                .size() > 0)
            return null;
        // check in lodging service
//        if (!isAvailable(request.getLodgeId(), request.getReservationStart(), request.getReservationEnd()))
//            return null;
        ReservationRequest rr = ReservationRequest.builder()
                .guestNumber(request.getGuestNumber())
                .reservationEnd(request.getReservationEnd())
                .reservationStart(request.getReservationStart())
                .id(null)
                .lodgeId(request.getLodgeId())
                .status(RequestStatus.PENDING)
                .totalPrice(request.getTotalPrice())
                .userId(request.getUserId())
                .build();
        return requestRepository.save(rr);
    }

    public List<ReservationRequest> getAll() {
        return requestRepository.findAll();
    }

}

package com.devops.bookingservice.controller;

import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/api/reservations")
public class ReservationController {


    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Reservation>> getAll() {
        List<Reservation> result = reservationService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/cancel")
    public ResponseEntity<Reservation> cancel(@PathVariable String id) {
        Reservation result = reservationService.cancel(id);
        if (result == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/cancel-count")
    public ResponseEntity<Integer> getCancelCountForUser(@PathVariable Integer id) {
        Integer count = reservationService.getCancelCountForUser(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}

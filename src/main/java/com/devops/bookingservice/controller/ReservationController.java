package com.devops.bookingservice.controller;

import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api/reservations")
@CrossOrigin("*")
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

    @GetMapping(value = "/{id}/active")
    public  ResponseEntity<List<Reservation>> getAllActiveByUser(@PathVariable Integer id) {
        List<Reservation> list = reservationService.getAllByUserIdActive(id);
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/all-reservations")
    public  ResponseEntity<List<Reservation>> getAllByUser(@PathVariable Integer id) {
        List<Reservation> list = reservationService.getAllByUserId(id);
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/lodge/{id}/active")
    public ResponseEntity<List<Reservation>> getAllActiveByLodge(@PathVariable Integer id) {
        List<Reservation> list = reservationService.getAllByLodgeIdActive(id);
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/lodges/active")
    public ResponseEntity<List<Reservation>> getAllActiveByLodges(@RequestParam List<Integer> ids) {
        List<Reservation> list = reservationService.getAllByLodgeIdsActive(ids);
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}

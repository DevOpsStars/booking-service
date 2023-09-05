package com.devops.bookingservice.controller;

import com.devops.bookingservice.dto.NewRequestDTO;
import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.services.ReservationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/requests")
@CrossOrigin("*")
public class ReservationRequestController {

    private final ReservationRequestService requestService;

    @Autowired
    public ReservationRequestController(ReservationRequestService requestService){
        this.requestService = requestService;
    }

    @PostMapping(value = "/send-request")
    public ResponseEntity<ReservationRequest> create(@RequestBody NewRequestDTO dto){
        ReservationRequest result = this.requestService.createNew(dto);
        if (result == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/send-request/automatic-accept")
    public ResponseEntity<Reservation> createAutomaticAccept(@RequestBody NewRequestDTO dto){
        ReservationRequest result = this.requestService.createNew(dto);
        if (result == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Reservation reservation = requestService.acceptRequest(result.getId());
        if (reservation == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping(value = "")
    public ResponseEntity<List<ReservationRequest>> getAll() {
        List<ReservationRequest> result = requestService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deletePending(@PathVariable String id) {
        int result = requestService.deletePending(id);
        if (result == -1)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (result == -2)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/accept")
    public ResponseEntity<Reservation> acceptPending(@PathVariable String id) {
        Reservation result = requestService.acceptRequest(id);
        if (result == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/decline")
    public ResponseEntity<ReservationRequest> declinePending(@PathVariable String id) {
        ReservationRequest result = requestService.declineRequest(id);
        if (result == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/pending")
    public ResponseEntity<List<ReservationRequest>> getPendingForLodge(@PathVariable Integer id) {
        List<ReservationRequest> result = requestService.getAllByLodgeIdPending(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

package com.devops.bookingservice.controller;

import com.devops.bookingservice.model.Notification;
import com.devops.bookingservice.model.NotificationStatus;
import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.services.NotificationService;
import com.devops.bookingservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Controller
@RequestMapping("/api/reservations")
@CrossOrigin("*")
public class ReservationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final ReservationService reservationService;

    private final NotificationService notificationService;

    @Autowired
    public ReservationController(ReservationService reservationService, NotificationService notificationService){
        this.reservationService = reservationService;
        this.notificationService = notificationService;
    }

    @GetMapping(value = "")
    public ResponseEntity<List<Reservation>> getAll() {
        List<Reservation> result = reservationService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/cancel/{hostId}")
    public ResponseEntity<Reservation> cancel(@PathVariable String id, @PathVariable Integer hostId) {
        Reservation result = reservationService.cancel(id);
        if (result == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Notification notification = Notification.builder()
                .status(NotificationStatus.CANCEL)
                .message("Reservation for Lodge with id " + result.getLodgeId() + " was canceled. Reservation id: " + id)
                .senderId(result.getUserId())
                .receiverId(hostId)
                .build();
        notificationService.save(notification);
        simpMessagingTemplate.convertAndSend("/topic/resCanceled", notification);
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

    @GetMapping(value = "/lodge/{id}/period/count")
    public ResponseEntity<Integer> getCountReservationsByPeriod(@PathVariable Integer id, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        Integer count = reservationService.getCountReservationsByPeriod(start, end, id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping(value = "/lodge/{id}/period")
    public ResponseEntity<List<Reservation>> getReservationsByPeriod(@PathVariable Integer id, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        List<Reservation> list = reservationService.getReservationsByPeriod(start, end, id);
        if (list.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}

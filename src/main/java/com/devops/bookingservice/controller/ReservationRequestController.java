package com.devops.bookingservice.controller;

import com.devops.bookingservice.dto.NewRequestDTO;
import com.devops.bookingservice.model.Notification;
import com.devops.bookingservice.model.NotificationStatus;
import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.services.NotificationService;
import com.devops.bookingservice.services.ReservationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

@Controller
@RequestMapping("/api/requests")
@CrossOrigin("*")
public class ReservationRequestController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final ReservationRequestService requestService;

    private final NotificationService notificationService;

    @Autowired
    public ReservationRequestController(ReservationRequestService requestService, NotificationService notificationService){
        this.requestService = requestService;
        this.notificationService = notificationService;
    }

    @PostMapping(value = "/send-request/{hostId}")
    public ResponseEntity<ReservationRequest> create(@PathVariable Integer hostId, @RequestBody NewRequestDTO dto){
        ReservationRequest result = this.requestService.createNew(dto);
        if (result == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Notification notification = Notification.builder()
                .status(NotificationStatus.CREATE)
                .message("New reservation request! User id: " + dto.getUserId() + ", Lodge id: "+ dto.getLodgeId())
                .senderId(dto.getUserId())
                .receiverId(hostId)
                .build();
        notificationService.save(notification);
        simpMessagingTemplate.convertAndSend("/topic/reqCreated",notification);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/send-request/automatic-accept/{hostId}")
    public ResponseEntity<Reservation> createAutomaticAccept(@PathVariable Integer hostId, @RequestBody NewRequestDTO dto){
        ReservationRequest result = this.requestService.createNew(dto);
        if (result == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Reservation reservation = requestService.acceptRequest(result.getId());
        if (reservation == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Notification notification = Notification.builder()
                .status(NotificationStatus.CREATE)
                .message("New automatically accepted reservation request! User id: " + dto.getUserId() + ", Lodge id: "+ dto.getLodgeId())
                .senderId(dto.getUserId())
                .receiverId(hostId)
                .build();
        notificationService.save(notification);
        simpMessagingTemplate.convertAndSend("/topic/reqCreated",notification);

        Notification guestNotification = Notification.builder()
                .status(NotificationStatus.RESPOND)
                .message("Reservation Request was accepted! Lodge id: " + result.getLodgeId())
                .senderId(hostId)
                .receiverId(result.getUserId())
                .build();
        notificationService.save(guestNotification);
        simpMessagingTemplate.convertAndSend("/topic/reqAccepted",guestNotification);
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
        Notification notification = Notification.builder()
                .status(NotificationStatus.RESPOND)
                .message("Reservation Request was accepted! Lodge id: " + result.getLodgeId())
                .senderId(0)
                .receiverId(result.getUserId())
                .build();
        notificationService.save(notification);
        simpMessagingTemplate.convertAndSend("/topic/reqAccepted",notification);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/decline")
    public ResponseEntity<ReservationRequest> declinePending(@PathVariable String id) {
        ReservationRequest result = requestService.declineRequest(id);
        if (result == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Notification notification = Notification.builder()
                .status(NotificationStatus.RESPOND)
                .message("Reservation Request was declined. Lodge id: " + result.getLodgeId())
                .senderId(0)
                .receiverId(result.getUserId())
                .build();
        notificationService.save(notification);
        simpMessagingTemplate.convertAndSend( "/topic/reqDeclined", notification);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/pending")
    public ResponseEntity<List<ReservationRequest>> getPendingForLodge(@PathVariable Integer id) {
        List<ReservationRequest> result = requestService.getAllByLodgeIdPending(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

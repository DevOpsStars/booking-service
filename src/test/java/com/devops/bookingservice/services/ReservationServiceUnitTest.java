package com.devops.bookingservice.services;

import com.devops.bookingservice.model.RequestStatus;
import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceUnitTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;


    @Test
    void givenValidRequest_whenCreateReservationFromRequest_thenReservationIsCreated() {
        // given
        ReservationRequest request = new ReservationRequest();
        request.setLodgeId(1);
        request.setUserId(1);
        request.setStatus(RequestStatus.ACCEPTED);
        request.setGuestNumber(3);
        request.setTotalPrice(13.99);
        request.setReservationStart(LocalDate.of(2023, 9, 11));
        request.setReservationEnd(LocalDate.of(2023, 9, 18));
        given(reservationRepository.save(any(Reservation.class))).willAnswer(answer -> answer.getArgument(0));

        // when
        Reservation reservation = reservationService.createReservationFromRequest(request);

        // then
        assertFalse(reservation.getCanceled());
        assertEquals(reservation.getUserId(), request.getUserId());
        assertEquals(reservation.getReservationStart(), request.getReservationStart());
        assertEquals(reservation.getReservationEnd(), request.getReservationEnd());
        assertEquals(reservation.getLodgeId(), request.getLodgeId());
        assertEquals(reservation.getGuestNumber(), request.getGuestNumber());
        assertEquals(reservation.getTotalPrice(), request.getTotalPrice());
    }

    @Test
    void givenPastStartDate_whenCancel_thenCancellationFails() {
        // given
        String id = "someId";
        Reservation existingReservation = Reservation.builder()
                .id(id)
                .canceled(false)
                .userId(1)
                .lodgeId(1)
                .guestNumber(3)
                .totalPrice(13.99)
                .reservationStart(LocalDate.of(2022, 1, 11))
                .reservationEnd(LocalDate.of(2022, 1, 18))
                .build();
        given(reservationRepository.findById(id)).willReturn(Optional.of(existingReservation));

        // when
        Reservation actualReservation = reservationService.cancel(id);

        // then
        assertNull(actualReservation);
    }

    @Test
    void givenStartDateTomorrow_whenCancel_thenCancellationFails() {
        // given
        String id = "someId";
        Reservation existingReservation = Reservation.builder()
                .id(id)
                .canceled(false)
                .userId(1)
                .lodgeId(1)
                .guestNumber(3)
                .totalPrice(13.99)
                .reservationStart(LocalDate.now().plusDays(1))
                .reservationEnd(LocalDate.now().plusDays(7))
                .build();
        given(reservationRepository.findById(id)).willReturn(Optional.of(existingReservation));

        // when
        Reservation actualReservation = reservationService.cancel(id);

        // then
        assertNull(actualReservation);
    }

    @Test
    void givenCanceledReservation_whenCancel_thenCancellationFails() {
        // given
        String id = "someId";
        Reservation existingReservation = Reservation.builder()
                .id(id)
                .canceled(true)
                .userId(1)
                .lodgeId(1)
                .guestNumber(3)
                .totalPrice(13.99)
                .reservationStart(LocalDate.of(2023, 9, 11))
                .reservationEnd(LocalDate.of(2023, 9, 18))
                .build();
        given(reservationRepository.findById(id)).willReturn(Optional.of(existingReservation));

        // when
        Reservation actualReservation = reservationService.cancel(id);

        // then
        assertNull(actualReservation);
    }

    @Test
    void givenValidId_whenCancel_thenReservationIsCanceled() {
        // given
        String id = "someId";
        Reservation existingReservation = Reservation.builder()
                .id(id)
                .canceled(false)
                .userId(1)
                .lodgeId(1)
                .guestNumber(3)
                .totalPrice(13.99)
                .reservationStart(LocalDate.of(2023, 9, 11))
                .reservationEnd(LocalDate.of(2023, 9, 18))
                .build();
        given(reservationRepository.findById(id)).willReturn(Optional.of(existingReservation));
        given(reservationRepository.save(any(Reservation.class))).willAnswer(answer -> answer.getArgument(0));

        // when
        Reservation actualReservation = reservationService.cancel(id);

        // then
        assertTrue(actualReservation.getCanceled());
        assertEquals(actualReservation.getId(), id);
    }



}
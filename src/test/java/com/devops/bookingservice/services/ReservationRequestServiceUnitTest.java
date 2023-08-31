package com.devops.bookingservice.services;

import com.devops.bookingservice.dto.NewRequestDTO;
import com.devops.bookingservice.model.RequestStatus;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.repository.ReservationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationRequestServiceUnitTest {

    @Mock
    private ReservationRequestRepository requestRepository;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationRequestService requestService;


    @Test
    void givenAcceptedReservationInSamePeriod_whenCreatingNewRequest_thenReturnsNull() {
        // given
        NewRequestDTO newRequest = new NewRequestDTO();
        newRequest.setReservationStart(LocalDate.now());
        newRequest.setReservationEnd(LocalDate.now().plusDays(3));

        List<ReservationRequest> acceptedRequests = new ArrayList<>();
        acceptedRequests.add(new ReservationRequest());
        given(requestRepository.findAcceptedByPeriod(newRequest.getReservationStart(), newRequest.getReservationEnd()))
                .willReturn(acceptedRequests);

        // when
        ReservationRequest result = requestService.createNew(newRequest);

        // then
        assertNull(result);
    }

    @Test
    void givenExistingRequests_whenGettingAll_thenReturnsAllRequests() {
        // given
        List<ReservationRequest> allRequests = new ArrayList<>();
        allRequests.add(new ReservationRequest());
        allRequests.add(new ReservationRequest());
        given(requestRepository.findAll()).willReturn(allRequests);

        // when
        List<ReservationRequest> result = requestService.getAll();

        // then
        assertEquals(allRequests.size(), result.size());
    }

    @Test
    void givenPendingRequest_whenDeletingPending_thenReturnsOne() {
        // given
        ReservationRequest request = new ReservationRequest();
        request.setStatus(RequestStatus.PENDING);
        given(requestRepository.findById("1")).willReturn(Optional.of(request));

        // when
        int result = requestService.deletePending("1");

        // then
        assertEquals(1, result);
    }

    @Test
    void givenNonExistentRequestId_whenDeletingPending_thenReturnsNegativeOne() {
        // given
        given(requestRepository.findById("1")).willReturn(Optional.empty());

        // when
        int result = requestService.deletePending("1");

        // then
        assertEquals(-1, result);
    }

    @Test
    void givenAcceptedRequest_whenDeletingPending_thenReturnsNegativeTwo() {
        // given
        ReservationRequest request = new ReservationRequest();
        request.setStatus(RequestStatus.ACCEPTED);
        given(requestRepository.findById("1")).willReturn(Optional.of(request));

        // when
        int result = requestService.deletePending("1");

        // then
        assertEquals(-2, result);
    }

}

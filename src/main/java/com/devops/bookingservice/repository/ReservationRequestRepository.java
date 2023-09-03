package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.ReservationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRequestRepository extends MongoRepository<ReservationRequest, String> {

    // TODO: add lodgeId to make it specific to lodge reservation requests
    @Query("{ 'reservationStart' : { $lt: ?1 }, 'reservationEnd' : { $gt: ?0 }, 'status': 'ACCEPTED', 'lodgeId' : ?2 }")
    List<ReservationRequest> findAcceptedByPeriod(LocalDate start, LocalDate end, Integer lodgeId);

    @Query("{ 'reservationStart' : { $lt: ?1 }, 'reservationEnd' : { $gt: ?0 }, 'status': 'PENDING', 'lodgeId' : ?2 }")
    List<ReservationRequest> findPendingByPeriod(LocalDate start, LocalDate end, Integer lodgeId);

    @Query("{'lodgeId' : ?0, 'status': 'PENDING' }")
    List<ReservationRequest> findAllByLodgeIdPending(Integer lodgeId);

}

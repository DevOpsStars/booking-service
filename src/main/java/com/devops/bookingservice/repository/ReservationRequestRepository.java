package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.ReservationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRequestRepository extends MongoRepository<ReservationRequest, String> {

    // TODO: add lodgeId to make it specific to lodge reservation requests
    @Query("{'reservationStart' : { $gte: ?0, $lte: ?1 }, 'reservationEnd' : {$gte: ?0, $lte: ?1}, 'status': 'ACCEPTED' }")
    List<ReservationRequest> findAcceptedByPeriod(LocalDate start, LocalDate end);

    @Query("{'lodgeId' : ?0, 'status': 'PENDING' }")
    List<ReservationRequest> findAllByLodgeIdPending(Integer lodgeId);

}

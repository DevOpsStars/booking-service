package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.ReservationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface ReservationRequestRepository extends MongoRepository<ReservationRequest, String> {

    @Query("{'reservationStart' : { $gte: ?0, $lte: ?1 }, 'reservationEnd' : {$gte: ?0, $lte: ?1}, 'status': 'ACCEPTED' }")
    List<ReservationRequest> findAcceptedByPeriod(Date start, Date end);

}

package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    @Query(value = "{'canceled' : true, 'userId' : ?0}", count = true)
    Integer countIsCanceledByUserId(Integer userId);
}

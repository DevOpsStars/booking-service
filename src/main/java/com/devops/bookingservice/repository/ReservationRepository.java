package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    @Query(value = "{'canceled' : true, 'userId' : ?0}", count = true)
    Integer countIsCanceledByUserId(Integer userId);

    List<Reservation> findAllByUserIdAndReservationEndAfter(Integer userId, LocalDate date);

    List<Reservation> findAllByUserId(Integer userId);

    List<Reservation> findAllByLodgeId(Integer userId);

    List<Reservation> findAllByLodgeIdIn(List<Integer> userId);
}

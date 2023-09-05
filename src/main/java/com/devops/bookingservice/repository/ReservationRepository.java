package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.model.ReservationRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    @Query(value = "{'canceled' : true, 'userId' : ?0}", count = true)
    Integer countIsCanceledByUserId(Integer userId);

    List<Reservation> findAllByUserIdAndReservationEndAfterAndCanceledIsFalse(Integer userId, LocalDate date);

    List<Reservation> findAllByUserIdAndCanceledIsFalse(Integer userId);

    List<Reservation> findAllByLodgeIdAndReservationEndAfterAndCanceledIsFalse(Integer userId, LocalDate date);

    List<Reservation> findAllByLodgeIdInAndReservationEndAfterAndCanceledIsFalse(List<Integer> userId, LocalDate date);

    @Query(value = "{ 'reservationStart' : { $lt: ?1 }, 'reservationEnd' : { $gt: ?0 }, 'lodgeId':  ?2, 'canceled': 'false' }", count = true)
    Integer countInPeriodByLodge(LocalDate start, LocalDate end, Integer lodgeId);

    @Query("{ 'reservationStart' : { $lt: ?1 }, 'reservationEnd' : { $gt: ?0 }, 'lodgeId' : ?2, 'canceled': 'false' }")
    List<ReservationRequest> findAcceptedByPeriod(LocalDate start, LocalDate end, Integer lodgeId);
}

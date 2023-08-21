package com.devops.bookingservice.repository;

import com.devops.bookingservice.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

}

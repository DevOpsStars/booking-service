package com.devops.bookingservice.repositories;

import com.devops.bookingservice.model.RequestStatus;
import com.devops.bookingservice.model.ReservationRequest;
import com.devops.bookingservice.repository.ReservationRequestRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
class ReservationRequestRepositoryIntegrationTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private ReservationRequestRepository repository;

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void givenDateRange_whenFindAcceptedByPeriod_thenReturnRequests() {
        // given
        LocalDate start = LocalDate.of(2023, 8, 19);
        LocalDate end = LocalDate.of(2023, 8, 25);

        ReservationRequest request = new ReservationRequest();
        request.setReservationStart(start);
        request.setReservationEnd(end);
        request.setLodgeId(1);
        request.setStatus(RequestStatus.ACCEPTED);
        repository.save(request);

        // when
        List<ReservationRequest> result = repository.findAcceptedByPeriod(start, end, 1);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getReservationStart()).isEqualTo(start);
        assertThat(result.get(0).getReservationEnd()).isEqualTo(end);
    }

    @Test
    void givenNoAcceptedRequestsInDateRange_whenFindAcceptedByPeriod_thenReturnEmptyList() {
        // given
        LocalDate start = LocalDate.of(2023, 9, 1);
        LocalDate end = LocalDate.of(2023, 9, 5);

        LocalDate start2 = LocalDate.of(2023, 8, 19);
        LocalDate end2 = LocalDate.of(2023, 8, 25);

        ReservationRequest request = new ReservationRequest();
        request.setReservationStart(start2);
        request.setReservationEnd(end2);
        request.setStatus(RequestStatus.ACCEPTED);
        repository.save(request);

        // when
        List<ReservationRequest> result = repository.findAcceptedByPeriod(start, end, 1);

        // then
        assertThat(result).isEmpty();
    }
}

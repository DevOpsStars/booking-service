package com.devops.bookingservice.repositories;

import com.devops.bookingservice.model.Reservation;
import com.devops.bookingservice.repository.ReservationRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@ActiveProfiles("test")
class ReservationRepositoryIntegrationTest {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");  // Adjust to your desired version
    private MongoDatabase database;

    @Autowired
    private ReservationRepository repository;

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @BeforeEach
    public void setup() {
        mongoDBContainer.start();
        MongoClient mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
        database = mongoClient.getDatabase("test");
    }

    @AfterEach
    public void cleanup() {
        for (String collectionName : database.listCollectionNames()) {
            database.getCollection(collectionName).deleteMany(new Document());
        }
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void givenUserIdAndCanceledReservations_whenCountIsCanceledByUserId_thenReturnCorrectCount() {
        // given
        Reservation r1 = new Reservation();
        r1.setUserId(42);
        r1.setCanceled(true);
        repository.save(r1);

        Reservation r2 = new Reservation();
        r2.setUserId(42);
        r2.setCanceled(true);
        repository.save(r2);

        // when
        Integer count = repository.countIsCanceledByUserId(42);

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void givenUserIdAndNoCanceledReservations_whenCountIsCanceledByUserId_thenReturnZero() {
        // given
        Reservation r = new Reservation();
        r.setUserId(42);
        r.setCanceled(false);
        repository.save(r);

        // when
        Integer count = repository.countIsCanceledByUserId(42);

        // then
        assertThat(count).isEqualTo(0);
    }
}

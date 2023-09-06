package com.devops.bookingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestBookingServiceApplication.class)
@ActiveProfiles("test")
class BookingServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}

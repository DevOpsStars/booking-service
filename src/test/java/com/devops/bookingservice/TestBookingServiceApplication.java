package com.devops.bookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(BookingServiceApplication::main).with(TestBookingServiceApplication.class).run(args);
	}

}

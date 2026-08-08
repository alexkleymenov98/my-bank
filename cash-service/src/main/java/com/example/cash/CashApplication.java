package com.example.cash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import shared.producer.NotificationProducer;

@SpringBootApplication
@Import(shared.configuration.SharedConfig.class)
public class CashApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashApplication.class, args);
	}

}

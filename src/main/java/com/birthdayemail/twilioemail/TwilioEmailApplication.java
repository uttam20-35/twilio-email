package com.birthdayemail.twilioemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class TwilioEmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwilioEmailApplication.class, args);
	}

}

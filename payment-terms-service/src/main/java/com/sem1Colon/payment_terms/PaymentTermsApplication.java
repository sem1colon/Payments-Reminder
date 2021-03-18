package com.sem1Colon.payment_terms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PaymentTermsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentTermsApplication.class, args);
	}

}

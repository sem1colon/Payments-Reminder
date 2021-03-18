package com.sem1Colon.payment_terms.repository;

import com.sem1Colon.payment_terms.entity.PaymentTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTermsRepository extends JpaRepository<PaymentTerm, Long>{
	
	PaymentTerm findByCode(String String);
}

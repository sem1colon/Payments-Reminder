package com.sem1Colon.payment_terms.controller;

import com.sem1Colon.payment_terms.entity.PaymentTerm;
import com.sem1Colon.payment_terms.exception.BadRequestException;
import com.sem1Colon.payment_terms.exception.RecordNotFoundException;
import com.sem1Colon.payment_terms.repository.PaymentTermsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PaymentTermController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentTermController.class);

	@Autowired
	PaymentTermsRepository paymentTermsRepository;

	/**
	 * Get All PaymentTerms
	 * @return PaymentTerms
	 */
	@GetMapping("/paymentTerms")
	public List<PaymentTerm> getAllPaymentTerms() {
		logger.info("GET: paymentTerms api called");
		return paymentTermsRepository.findAll();
	}

	/**
	 * Create a new PaymentTerm
	 * @param paymentTerm
	 * @return http response
	 */
	@PostMapping("/paymentTerms")
	public ResponseEntity<PaymentTerm> createPaymentTerm(@Valid @RequestBody PaymentTerm paymentTerm) {
		logger.info("post: paymentTerms api called");
		if (paymentTerm.getDays() < paymentTerm.getRemindBeforeDays()) {
			throw new BadRequestException("RemindBeforeDays should not greater than Days field");
		}
		paymentTerm = paymentTermsRepository.save(paymentTerm);
		return new ResponseEntity<PaymentTerm>(paymentTerm, HttpStatus.OK);
	}

	/**
	 * Get a Single PaymentTerm
	 * @param paymentTermId
	 * @return http response
	 */
	@GetMapping("/paymentTerms/{id}")
	public ResponseEntity<PaymentTerm> getPaymentTermById(@PathVariable(value = "id") Long paymentTermId) {
		PaymentTerm paymentTerm = paymentTermsRepository.findById(paymentTermId)
				.orElseThrow(() -> new RecordNotFoundException("PaymentTerm id : " + paymentTermId + " does no exist"));
		return new ResponseEntity<PaymentTerm>(paymentTerm, HttpStatus.OK);
	}

	/**
	 * Update a PaymentTerm
	 * @param paymentTermId
	 * @param paymentTermDetails
	 * @return http response
	 */
	@PutMapping("/paymentTerms/{id}")
	public ResponseEntity<PaymentTerm> updatePaymentTerm(@PathVariable(value = "id") Long paymentTermId,
			@RequestBody PaymentTerm paymentTermDetails) {

		PaymentTerm paymentTerm = paymentTermsRepository.findById(paymentTermId)
				.orElseThrow(() -> new RecordNotFoundException("PaymentTerm id : " + paymentTermId + " does no exist"));

		if (!StringUtils.isEmpty(paymentTermDetails.getCode())) {
			paymentTerm.setCode(paymentTermDetails.getCode());
		}
		if (!StringUtils.isEmpty(paymentTermDetails.getDescription())) {
			paymentTerm.setDescription(paymentTermDetails.getDescription());
		}
		if (paymentTermDetails.getDays() != null) {
			paymentTerm.setDays(paymentTermDetails.getDays());
		}

		if (paymentTermDetails.getRemindBeforeDays() != null) {
			paymentTerm.setRemindBeforeDays(paymentTermDetails.getRemindBeforeDays());
		}

		if (paymentTerm.getDays() < paymentTerm.getRemindBeforeDays()) {
			throw new BadRequestException("RemindBeforeDays should not greater than Days field");
		}

		PaymentTerm updatedPaymentTerm = paymentTermsRepository.save(paymentTerm);
		return new ResponseEntity<PaymentTerm>(updatedPaymentTerm, HttpStatus.OK);
	}

	/**
	 * Delete a PaymentTerm
	 * @param paymentTermId
	 * @return http response
	 */
	@DeleteMapping("/paymentTerms/{id}")
	public ResponseEntity<?> deletePaymentTerm(@PathVariable(value = "id") Long paymentTermId) {
		PaymentTerm paymentTerm = paymentTermsRepository.findById(paymentTermId)
				.orElseThrow(() -> new RecordNotFoundException("PaymentTerm id : " + paymentTermId + " does no exist"));
		paymentTermsRepository.delete(paymentTerm);

		return ResponseEntity.ok().build();
	}

}

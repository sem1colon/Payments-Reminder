package com.sem1Colon.invoice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sem1Colon.invoice.dto.PaymentTermDTO;
import com.sem1Colon.invoice.entity.Invoice;
import com.sem1Colon.invoice.repository.InvoiceRepository;

@Component
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	RestTemplate restTemplate;

	static String paymentTermsURL = "http://localhost:8080/api/v1/paymentTerms";

	// for every minute cron job will run
	// @Scheduled(cron = "0 * * * * ?")

	// for every day 1 am cron job will run
	@Scheduled(cron = "0 1 * * * ?")
	public void scheduleTaskWithCronExpression() {
		logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

		List<Invoice> invoiceList = invoiceRepository.findByStatus("UNPAID");

		if (invoiceList != null && invoiceList.size() > 0) {
			try {
				List<PaymentTermDTO> paymentTermsList = getPaymentTermList();
				Map<String, PaymentTermDTO> paymentTermsMap = new HashMap<String, PaymentTermDTO>();
				if (paymentTermsList != null && paymentTermsList.size() > 0) {
					for (PaymentTermDTO paymentTerm : paymentTermsList) {
						paymentTermsMap.put(paymentTerm.getCode(), paymentTerm);
					}
				}

				if (paymentTermsMap.size() > 0) {
					for (Invoice invoice : invoiceList) {
						try {
							Boolean sendNotification = validateInvoiceNotificationDate(paymentTermsMap, invoice);
							if (sendNotification) {
								logger.info("Reminder sent for Invoice" + invoice.getInvoiceNumber());
							}
						} catch (Exception e) {
							logger.info("Error occured while processing invoice" + invoice.getInvoiceNumber());
						}
					}
				} else {
					logger.info("PaymentTerms are empty. please contact admin");
				}
			} catch (Exception e) {
				logger.info("Error occured while getting paymentTerms");
			}
		}
	}

	protected Boolean validateInvoiceNotificationDate(Map<String, PaymentTermDTO> paymentTermsMap, Invoice invoice) {
		Boolean sendNotification = false;
		PaymentTermDTO paymentTerm = paymentTermsMap.get(invoice.getPaymentTerm());
		if (paymentTerm != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			Date todatDate = calendar.getTime();
			logger.info("Today date " + todatDate);

			calendar.setTime(invoice.getInvoiceDate());
			calendar.add(Calendar.DATE, paymentTerm.getDays());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date invoicePaymentDate = calendar.getTime();
			logger.info("Invoice Payment Date " + invoicePaymentDate);

			calendar.setTime(invoicePaymentDate);
			calendar.add(Calendar.DATE, -paymentTerm.getRemindBeforeDays());
			Date invoicePaymentRemindDate = calendar.getTime();
			logger.info("Invoice Payment Remind Date " + invoicePaymentRemindDate);

			if (todatDate.compareTo(invoicePaymentRemindDate) == 0) {
				sendNotification = true;
			}
		} else {
			logger.info("Invoice PaymentTerm doesn't exist in PaymentTerm Table" + invoice.getPaymentTerm());
		}
		;
		return sendNotification;
	}

	protected List<PaymentTermDTO> getPaymentTermList() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		PaymentTermDTO[] paymentTermList = restTemplate.getForObject(paymentTermsURL, PaymentTermDTO[].class);
		return Arrays.asList(paymentTermList);
	}

}

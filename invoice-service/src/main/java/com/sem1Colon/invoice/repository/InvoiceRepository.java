package com.sem1Colon.invoice.repository;

import java.util.List;

import com.sem1Colon.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String>{
	
	List<Invoice> findByStatus(String status);
	
	Invoice findByInvoiceNumber(String invoiceNumber);
	
}

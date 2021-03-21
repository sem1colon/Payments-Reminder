package com.sem1Colon.invoice.dto;

import java.util.Date;

public class PaymentTermDTO {

	private Long id;

	private String code;

	private String description;

	private Integer days;

	private Integer remindBeforeDays;

	private Date createdAt;

	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public Integer getRemindBeforeDays() {
		return remindBeforeDays;
	}

	public void setRemindBeforeDays(Integer remindBeforeDays) {
		this.remindBeforeDays = remindBeforeDays;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "PaymentTerm [code= " + code + ", description= " + description + ", days = " + days + ", remindBeforeDays = "
				+ remindBeforeDays + "]";
	}

}

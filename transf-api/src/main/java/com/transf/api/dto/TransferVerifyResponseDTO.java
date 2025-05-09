package com.transf.api.dto;

import java.math.BigDecimal;

public class TransferVerifyResponseDTO {
	private boolean valid;
	private String message;
	private String fromAccountName;
	private String fromAccountNumber;
	private String toAccountName;
	private String toAccountNumber;
	private BigDecimal amount;

	public TransferVerifyResponseDTO(boolean valid, String message, String fromAccountName, String fromAccountNumber,
			String toAccountName, String toAccountNumber, BigDecimal amount) {
		super();
		this.valid = valid;
		this.message = message;
		this.fromAccountName = fromAccountName;
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountName = toAccountName;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromAccountName() {
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}

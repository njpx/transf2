package com.transf.api.dto;

import jakarta.validation.constraints.NotBlank;

public class TransferRequestDTO extends TransactionRequestDTO {

	@NotBlank
	private String fromAccountNumber;

	@NotBlank
	private String pin;

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

}

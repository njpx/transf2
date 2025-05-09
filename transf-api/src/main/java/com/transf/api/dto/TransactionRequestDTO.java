package com.transf.api.dto;

import java.math.BigDecimal;

import com.transf.api.model.TransactionChannel;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public abstract class TransactionRequestDTO {

	@NotBlank
	private String toAccountNumber;

	@NotNull
	@DecimalMin("1.00")
	private BigDecimal amount;

	@NotNull
	private TransactionChannel channel;

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

	public TransactionChannel getChannel() {
		return channel;
	}

	public void setChannel(TransactionChannel channel) {
		this.channel = channel;
	}

}

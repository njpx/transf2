package com.transf.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
	private String fromAccountNumber;
	private String toAccountNumber;
	private BigDecimal amount;
	private String code;
	private String channel;
	private String remarks;
	private LocalDateTime timestamp;
	private BigDecimal postBalance;

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public BigDecimal getPostBalance() {
		return postBalance;
	}

	public void setPostBalance(BigDecimal postBalance) {
		this.postBalance = postBalance;
	}

}

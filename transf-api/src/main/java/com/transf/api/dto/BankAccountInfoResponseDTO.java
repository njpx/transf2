package com.transf.api.dto;

import java.math.BigDecimal;

public class BankAccountInfoResponseDTO {
	String accountNumber;
	BigDecimal balance;

	public BankAccountInfoResponseDTO(String accountNumber, BigDecimal balance) {
		super();
		this.accountNumber = accountNumber;
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}

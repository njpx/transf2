package com.transf.api.model;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Transaction {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private BankAccount fromAccount;

	@ManyToOne
	private BankAccount toAccount;

	private BigDecimal amount;

	private Instant timestamp;

	@Enumerated(EnumType.STRING)
	private TransactionCode type;

	@Enumerated(EnumType.STRING)
	private TransactionChannel channel;

	private BigDecimal postBalance;

	private String remarks;

	@ManyToOne
	private User owner;

	@ManyToOne
	private BankAccount account;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BankAccount getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(BankAccount fromAccount) {
		this.fromAccount = fromAccount;
	}

	public BankAccount getToAccount() {
		return toAccount;
	}

	public void setToAccount(BankAccount toAccount) {
		this.toAccount = toAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public TransactionCode getType() {
		return type;
	}

	public void setType(TransactionCode type) {
		this.type = type;
	}

	public TransactionChannel getChannel() {
		return channel;
	}

	public void setChannel(TransactionChannel channel) {
		this.channel = channel;
	}

	public BigDecimal getPostBalance() {
		return postBalance;
	}

	public void setPostBalance(BigDecimal postBalance) {
		this.postBalance = postBalance;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public BankAccount getAccount() {
		return account;
	}

	public void setAccount(BankAccount account) {
		this.account = account;
	}

}

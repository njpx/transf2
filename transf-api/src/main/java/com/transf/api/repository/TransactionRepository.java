package com.transf.api.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transf.api.model.BankAccount;
import com.transf.api.model.Transaction;
import com.transf.api.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByOwner(User owner);

	List<Transaction> findByAccountAndOwnerAndTimestampBetween(BankAccount bankAccount, User owner, Instant fromDate,
			Instant toDate);

}

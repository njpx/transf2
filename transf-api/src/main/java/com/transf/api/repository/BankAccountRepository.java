package com.transf.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transf.api.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
	boolean existsByAccountNumber(String accountNumber);

	Optional<BankAccount> findByAccountNumber(String toAccountNumber);
	
	Optional<List<BankAccount>> findByOwnerIdOrderByCreatedAtAsc(Long id);
	
}

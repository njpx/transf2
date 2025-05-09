package com.transf.api.service;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.transf.api.dto.BankAccountInfoResponseDTO;
import com.transf.api.dto.CreateAccountRequestDTO;
import com.transf.api.error.CustomException;
import com.transf.api.model.BankAccount;
import com.transf.api.model.Transaction;
import com.transf.api.model.TransactionChannel;
import com.transf.api.model.TransactionCode;
import com.transf.api.model.User;
import com.transf.api.model.UserRole;
import com.transf.api.repository.BankAccountRepository;
import com.transf.api.repository.TransactionRepository;
import com.transf.api.repository.UserRepository;

@Service
public class AccountService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BankAccountRepository bankAccountRepository;
	@Autowired
	private TransactionRepository transactionRepository;

	public AccountService(UserRepository userRepository, BankAccountRepository bankAccountRepository) {
		this.userRepository = userRepository;
		this.bankAccountRepository = bankAccountRepository;
	}

	public String createAccount(CreateAccountRequestDTO dto) {
		User customer = userRepository.findById(dto.getCustomerId()).orElseThrow(
				() -> new CustomException("USER_NOT_FOUND_ERROR", "User not found", HttpStatus.NOT_FOUND));

		if (customer.getRole() != UserRole.CUSTOMER) {
			throw new CustomException("NOT_CUSTOMER_ERROR", "User is not a valid customer", HttpStatus.BAD_REQUEST);
		}

		String accountNumber = generateUniqueAccountNumber();

		BankAccount account = new BankAccount();
		account.setAccountNumber(accountNumber);
		account.setOwner(customer);
		account.setBalance(dto.getInitialDeposit());
		account.setActive(true);
		account.setCreatedAt(Instant.now());

		bankAccountRepository.save(account);

		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber).orElseThrow(
				() -> new CustomException("BANK_ACCOUNT_NOT_FOUND_ERROR", "Account not found", HttpStatus.NOT_FOUND));

		bankAccount.setBalance(bankAccount.getBalance());
		bankAccountRepository.save(bankAccount);

		Transaction txn = new Transaction();
		txn.setType(TransactionCode.DEPOSIT);
		txn.setChannel(TransactionChannel.TELLER);
		txn.setToAccount(bankAccount);
		txn.setAmount(dto.getInitialDeposit());
		txn.setTimestamp(Instant.now());
		txn.setPostBalance(bankAccount.getBalance());
		txn.setRemarks("New Account");
		txn.setOwner(bankAccount.getOwner());
		txn.setAccount(bankAccount);
		transactionRepository.save(txn);
		return accountNumber;
	}

	private String generateUniqueAccountNumber() {
		String number;
		do {
			number = String.format("%07d", new Random().nextInt(10_000_000));
		} while (bankAccountRepository.existsByAccountNumber(number));
		return number;
	}

	public List<BankAccountInfoResponseDTO> getBankAccountsByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		List<BankAccount> accounts = bankAccountRepository.findByOwnerIdOrderByCreatedAtAsc(user.getId()).orElseThrow(
				() -> new CustomException("BANK_ACCOUNT_NOT_FOUND_ERROR", "No Account Found", HttpStatus.NOT_FOUND));

		return accounts.stream()
				.map(account -> new BankAccountInfoResponseDTO(account.getAccountNumber(), account.getBalance()))
				.toList();
	}
}

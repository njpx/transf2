package com.transf.api.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.transf.api.dto.TransactionDTO;
import com.transf.api.dto.TransferVerifyResponseDTO;
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
public class TransactionService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BankAccountRepository bankAccountRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public TransferVerifyResponseDTO verifyTransfer(String fromAccount, String toAccount, BigDecimal amount) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new CustomException("USER_NOT_FOUND_ERROR", "User not found", HttpStatus.UNAUTHORIZED));

		if (user.getRole() != UserRole.CUSTOMER) {
			return new TransferVerifyResponseDTO(false, "Only customers can transfer", null, null, null, null, null);
		}

		if (fromAccount.equals(toAccount)) {
			return new TransferVerifyResponseDTO(false, "Cannot transfer to the same account", null, null, null, null,
					null);
		}

		BankAccount from = bankAccountRepository.findByAccountNumber(fromAccount).orElse(null);
		if (from == null || !from.getOwner().getId().equals(user.getId())) {
			return new TransferVerifyResponseDTO(false, "Invalid or unauthorized from-account", null, null, null, null,
					null);
		}

		BankAccount to = bankAccountRepository.findByAccountNumber(toAccount).orElse(null);
		if (to == null) {
			return new TransferVerifyResponseDTO(false, "Recipient account not found", null, null, null, null, null);
		}

		if (from.getBalance().compareTo(amount) < 0) {
			return new TransferVerifyResponseDTO(false, "Insufficient funds", null, null, null, null, null);
		}

		return new TransferVerifyResponseDTO(true, "Valid transfer", from.getOwner().getThaiName(),
				from.getAccountNumber(), to.getOwner().getThaiName(), to.getAccountNumber(), amount);
	}

	public void deposit(String toAccountNumber, BigDecimal amount, TransactionChannel channel) {
		BankAccount to = bankAccountRepository.findByAccountNumber(toAccountNumber)
				.orElseThrow(() -> new CustomException("BANK_ACCOUNT_NOT_FOUND_ERROR", "Bank Account not found",
						HttpStatus.NOT_FOUND));

		to.setBalance(to.getBalance().add(amount));
		bankAccountRepository.save(to);

		Transaction txn = new Transaction();
		txn.setType(TransactionCode.DEPOSIT);
		txn.setChannel(channel);
		txn.setToAccount(to);
		txn.setAmount(amount);
		txn.setTimestamp(Instant.now());
		txn.setPostBalance(to.getBalance());
		txn.setRemarks("Deposit");
		txn.setOwner(to.getOwner());
		txn.setAccount(to);
		transactionRepository.save(txn);
	}

	public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount,
			TransactionChannel channel, String pin) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new CustomException("USER_NOT_FOUND_ERROR", "User not found", HttpStatus.UNAUTHORIZED));

		if (user.getRole() != UserRole.CUSTOMER) {
			throw new CustomException("NOT_CUSTOMER_ERROR", "Only customers can transfer funds",
					HttpStatus.UNAUTHORIZED);
		}

		if (!passwordEncoder.matches(pin, user.getPinHash())) {
			throw new CustomException("INVALID_PIN_ERROR", "Invalid PIN", HttpStatus.UNAUTHORIZED);
		}

		BankAccount from = bankAccountRepository.findByAccountNumber(fromAccountNumber)
				.orElseThrow(() -> new CustomException("SENDER_BANK_ACCOUNT_NOT_FOUND_ERROR",
						"Sender account not found", HttpStatus.NOT_FOUND));

		if (!from.getOwner().getId().equals(user.getId())) {
			throw new CustomException("NOT_BANK_ACCOUNT_OWNER_ERROR", "You do not own this account",
					HttpStatus.UNAUTHORIZED);
		}

		BankAccount to = bankAccountRepository.findByAccountNumber(toAccountNumber)
				.orElseThrow(() -> new CustomException("RECIPIENT_BANK_ACCOUNT_NOT_FOUND_ERROR", "To-account not found",
						HttpStatus.NOT_FOUND));

		if (from.getBalance().compareTo(amount) < 0) {
			throw new CustomException("INSUFFICIENT_FUNDS_ERROR", "Insufficient funds", HttpStatus.BAD_REQUEST);
		}

		from.setBalance(from.getBalance().subtract(amount));
		to.setBalance(to.getBalance().add(amount));

		bankAccountRepository.save(from);
		bankAccountRepository.save(to);

		// Outgoing
		Transaction outTxn = new Transaction();
		outTxn.setType(TransactionCode.TRANSFER);
		outTxn.setChannel(channel);
		outTxn.setFromAccount(from);
		outTxn.setToAccount(to);
		outTxn.setAmount(amount);
		outTxn.setTimestamp(Instant.now());
		outTxn.setPostBalance(from.getBalance());
		outTxn.setRemarks("Transfer to x" + last4DigitsAccountNumber(to.getAccountNumber()) + " "
				+ to.getOwner().getEnglishName());
		outTxn.setOwner(from.getOwner());
		outTxn.setAccount(from);
		transactionRepository.save(outTxn);

		// Incoming
		Transaction inTxn = new Transaction();
		inTxn.setType(TransactionCode.DEPOSIT);
		inTxn.setChannel(channel);
		inTxn.setFromAccount(from);
		inTxn.setToAccount(to);
		inTxn.setAmount(amount);
		inTxn.setTimestamp(Instant.now());
		inTxn.setPostBalance(to.getBalance());
		inTxn.setRemarks("Received from x" + last4DigitsAccountNumber(from.getAccountNumber()) + " "
				+ from.getOwner().getEnglishName());
		inTxn.setOwner(to.getOwner());
		inTxn.setAccount(to);
		transactionRepository.save(inTxn);
	}

	private String last4DigitsAccountNumber(String account) {
		return account.length() >= 4 ? account.substring(account.length() - 4) : "****";
	}

	private TransactionDTO mapToDTO(Transaction txn, String zoneId) {
		TransactionDTO dto = new TransactionDTO();
		dto.setFromAccountNumber(txn.getFromAccount() != null ? txn.getFromAccount().getAccountNumber() : null);
		dto.setToAccountNumber(txn.getToAccount() != null ? txn.getToAccount().getAccountNumber() : null);
		dto.setAmount(txn.getAmount());
		dto.setCode(txn.getType().name());
		dto.setChannel(txn.getChannel().name());
		dto.setRemarks(txn.getRemarks());
		LocalDateTime localDateTime = txn.getTimestamp().atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of(zoneId))
				.toLocalDateTime();
		dto.setTimestamp(localDateTime);
		dto.setPostBalance(txn.getPostBalance());
		return dto;
	}

	public List<TransactionDTO> getStatements(String bankAccountNumber, LocalDate fromDate, LocalDate toDate) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new CustomException("USER_NOT_FOUND_ERROR", "User not found", HttpStatus.UNAUTHORIZED));
		BankAccount bankAccount = bankAccountRepository.findByAccountNumber(bankAccountNumber)
				.orElseThrow(() -> new CustomException("BANK_ACCOUNT_NOT_FOUND_ERROR", "Bank account not found",
						HttpStatus.NOT_FOUND));

		if (!bankAccount.getOwner().getId().equals(user.getId())) {
			throw new CustomException("NOT_BANK_ACCOUNT_OWNER_ERROR", "You do not own this account",
					HttpStatus.UNAUTHORIZED);
		}
		String clientZoneId = user.getTimezone();

		// Start of day in user’s local time converted to UTC
		Instant fromInstant = fromDate.atStartOfDay(ZoneId.of(clientZoneId)).toInstant();

		// End of day in user’s local time converted to UTC
		Instant toInstant = toDate.plusDays(1).atStartOfDay(ZoneId.of(clientZoneId)).minusNanos(1).toInstant();

		List<Transaction> transactions = transactionRepository.findByAccountAndOwnerAndTimestampBetween(bankAccount,
				user, fromInstant, toInstant);

		return transactions.stream().map(trx -> mapToDTO(trx, clientZoneId)).toList();
	}
}

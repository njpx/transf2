package com.transf.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transf.api.dto.DepositRequestDTO;
import com.transf.api.dto.TransactionDTO;
import com.transf.api.dto.TransferRequestDTO;
import com.transf.api.dto.TransferVerifyRequestDTO;
import com.transf.api.dto.TransferVerifyResponseDTO;
import com.transf.api.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@PostMapping("/deposit")
	public ResponseEntity<Void> deposit(@Valid @RequestBody DepositRequestDTO dto) {
		transactionService.deposit(dto.getToAccountNumber(), dto.getAmount(), dto.getChannel());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/verify")
	public ResponseEntity<TransferVerifyResponseDTO> verify(@Valid @RequestBody TransferVerifyRequestDTO dto) {
		TransferVerifyResponseDTO result = transactionService.verifyTransfer(dto.getFromAccountNumber(),
				dto.getToAccountNumber(), dto.getAmount());
		return ResponseEntity.ok(result);
	}

	@PostMapping("/transfer")
	public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequestDTO dto) {
		transactionService.transfer(dto.getFromAccountNumber(), dto.getToAccountNumber(), dto.getAmount(),
				dto.getChannel(), dto.getPin());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/statements")
	public ResponseEntity<List<TransactionDTO>> getStatements(
			@RequestParam("bankAccountNumber") String bankAccountNumber,
			@RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		return ResponseEntity.ok(transactionService.getStatements(bankAccountNumber, fromDate, toDate));
	}
}

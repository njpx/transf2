package com.transf.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transf.api.dto.BankAccountInfoResponseDTO;
import com.transf.api.dto.CreateAccountRequestDTO;
import com.transf.api.error.CustomException;
import com.transf.api.repository.UserRepository;
import com.transf.api.service.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> createAccount(@Valid @RequestBody CreateAccountRequestDTO dto) {
		String accountNumber = accountService.createAccount(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("accountNumber", accountNumber));
	}

	@GetMapping("/my-bank-accounts")
	public ResponseEntity<List<BankAccountInfoResponseDTO>> getCurrentUserInfo() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		userRepository.findByEmail(email).orElseThrow(
				() -> new CustomException("USER_NOT_FOUND_ERROR", "User not found", HttpStatus.UNAUTHORIZED));

		List<BankAccountInfoResponseDTO> accounts = accountService.getBankAccountsByEmail(email);
		return ResponseEntity.ok(accounts);

	}
}

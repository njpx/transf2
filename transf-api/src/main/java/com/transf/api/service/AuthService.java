package com.transf.api.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.transf.api.dto.LoginRequestDTO;
import com.transf.api.dto.LoginResponseDTO;
import com.transf.api.dto.RegisterRequestDTO;
import com.transf.api.error.CustomException;
import com.transf.api.model.User;
import com.transf.api.model.UserRole;
import com.transf.api.repository.UserRepository;
import com.transf.api.security.JwtTokenProvider;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public void register(RegisterRequestDTO dto) {
		if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new CustomException("DUPLICATE_EMAIL_ERROR", "Email is already registered", HttpStatus.CONFLICT);
		}

		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setIdCardNumber(dto.getIdCardNumber());
		user.setThaiName(dto.getThaiName());
		user.setEnglishName(dto.getEnglishName());
		user.setPinHash(passwordEncoder.encode(dto.getPin()));
		user.setRole(UserRole.CUSTOMER);
		user.setActive(true);
		user.setTimezone("Asia/Bangkok");
		user.setCreatedAt(Instant.now());

		userRepository.save(user);
	}

	public LoginResponseDTO login(LoginRequestDTO dto) {
		User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
				() -> new CustomException("INVALID_CREDENTIALS_ERROR", "Invalid credentials", HttpStatus.BAD_REQUEST)); // TO hide root cause of Login error

		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			throw new CustomException("INVALID_CREDENTIALS_ERROR", "Invalid credentials", HttpStatus.BAD_REQUEST);
		}

		String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());

		return new LoginResponseDTO(token, user.getRole().name());
	}
}

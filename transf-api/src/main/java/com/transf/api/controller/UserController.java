package com.transf.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transf.api.dto.UserResponseDTO;
import com.transf.api.error.CustomException;
import com.transf.api.model.User;
import com.transf.api.repository.UserRepository;
import com.transf.api.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/my-profile")
	public ResponseEntity<Map<String, String>> getCurrentUserInfo() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByEmail(email).orElseThrow(
				() -> new CustomException("USER_NOT_FOUND_ERROR", "User not found", HttpStatus.UNAUTHORIZED));

		Map<String, String> response = new HashMap<>();
		response.put("thaiName", user.getThaiName());
		response.put("englishName", user.getEnglishName());
		response.put("role", user.getRole().name());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String keyword) {
		List<UserResponseDTO> results = userService.searchUsers(keyword);
		return ResponseEntity.ok(results);
	}
}

package com.transf.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transf.api.dto.UserResponseDTO;
import com.transf.api.model.User;
import com.transf.api.model.UserRole;
import com.transf.api.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<UserResponseDTO> searchUsers(String keyword) {
		List<User> users = userRepository.searchByKeywordAndRole(keyword, UserRole.CUSTOMER);

		return users.stream()
				.map(u -> new UserResponseDTO(u.getId(), u.getIdCardNumber(), u.getThaiName(), u.getEnglishName()))
				.toList();
	}
}
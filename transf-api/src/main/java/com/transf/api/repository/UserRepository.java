package com.transf.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.transf.api.model.User;
import com.transf.api.model.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	List<User> findByThaiNameContainingOrEnglishNameContainingOrIdCardNumberContaining(String thaiName,
			String englishName, String idCardNumber);

	@Query("SELECT u FROM User u WHERE u.role = :role AND ("
			+ "LOWER(u.thaiName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(u.englishName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
			+ "LOWER(u.idCardNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	List<User> searchByKeywordAndRole(@Param("keyword") String keyword, @Param("role") UserRole role);
}

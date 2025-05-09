package com.transf.api.seeder;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.transf.api.model.User;
import com.transf.api.model.UserRole;
import com.transf.api.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		final String tellerEmail = "teller@teller";
		if (userRepository.findByEmail(tellerEmail).isEmpty()) {
			User teller = new User();
			teller.setEmail(tellerEmail);
			teller.setPassword(passwordEncoder.encode("teller1234"));
			teller.setIdCardNumber("3647389098473");
			teller.setThaiName("พนักงานคนหนึ่ง");
			teller.setEnglishName("Teller A");
			teller.setPinHash(passwordEncoder.encode("999999"));
			teller.setRole(UserRole.TELLER);
			teller.setActive(true);
			teller.setTimezone("Asia/Bangkok");
			teller.setCreatedAt(Instant.now());

			userRepository.save(teller);
			System.out.println("✅ Default Teller created: " + tellerEmail);
		}

		final String customerEmail = "cust1@cust1";
		if (userRepository.findByEmail(customerEmail).isEmpty()) {
			User customer = new User();
			customer.setEmail(customerEmail);
			customer.setPassword(passwordEncoder.encode("abc12345"));
			customer.setIdCardNumber("8767890987655");
			customer.setThaiName("ลุกค้าท่านหนึ่ง");
			customer.setEnglishName("Customer A");
			customer.setPinHash(passwordEncoder.encode("999999"));
			customer.setRole(UserRole.CUSTOMER);
			customer.setActive(true);
			customer.setTimezone("Asia/Bangkok");
			customer.setCreatedAt(Instant.now());

			userRepository.save(customer);
			System.out.println("✅ Default Customer created: " + customerEmail);
		}
		
		final String customer2Email = "cust2@cust2";
		if (userRepository.findByEmail(customer2Email).isEmpty()) {
			User customer = new User();
			customer.setEmail(customer2Email);
			customer.setPassword(passwordEncoder.encode("abc12345"));
			customer.setIdCardNumber("3452348790654");
			customer.setThaiName("ลุกค้าท่านที่สอง");
			customer.setEnglishName("Customer B");
			customer.setPinHash(passwordEncoder.encode("999999"));
			customer.setRole(UserRole.CUSTOMER);
			customer.setActive(true);
			customer.setTimezone("Asia/Bangkok");
			customer.setCreatedAt(Instant.now());

			userRepository.save(customer);
			System.out.println("✅ Default Customer created: " + customer2Email);
		}
	}
}

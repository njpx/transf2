package com.transf.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static org.springframework.security.config.Customizer.withDefaults;
import com.transf.api.model.UserRole;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Value("${allow.origin.url}")
	private String allowOriginUrl;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*").allowedHeaders("*").allowedOrigins(allowOriginUrl)
						.allowCredentials(true);
			}
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors(withDefaults()).csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						requests -> requests.requestMatchers("/api/auth/login", "/api/auth/register").anonymous()
								.requestMatchers("/api/auth/register", "/api/bank-accounts/create",
										"/api/transactions/deposit", "/api/users/search")
								.hasAuthority(UserRole.TELLER.name()) //
								.requestMatchers("/api/transactions/verify", "/api/transactions/transfer",
										"/api/transactions/statements", "/api/bank-accounts/my-bank-accounts") //
								.hasAuthority(UserRole.CUSTOMER.name()) //
								.requestMatchers("/api/users/my-profile").authenticated() //
								.anyRequest().authenticated());

		http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	JwtAuthFilter jwtAuthFilter() {
		return new JwtAuthFilter(jwtTokenProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
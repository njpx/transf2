package com.transf.api.security;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	public JwtAuthFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			if (jwtTokenProvider.validateToken(token)) {
				String email = jwtTokenProvider.getClaimFromToken(token, "subject");
				String role = jwtTokenProvider.getClaimFromToken(token, "role");

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,
						List.of(new SimpleGrantedAuthority(role)));

				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				// Token invalid or expired - send 401 with JSON body
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json");
				Map<String, String> errorBody = Map.of("errorCode", "TOKEN_EXPIRED_ERROR", "message",
						"Token is invalid or expired");

				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(response.getWriter(), errorBody);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}

package com.transf.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.transf.api.error.CustomException;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

	@Value("${jwt.base64.secret}")
	private String base64Secret;

	@Value("${jwt.expiration.minutes}")
	private String expirationMinutes;

	public String generateToken(String email, String role) {
		try {
			Date currentDate = new Date();
			Date expiryDate = DateUtils.addMinutes(new Date(), Integer.parseInt(expirationMinutes));

			// Creates a MessageDigest object for the SHA-256 algorithm, used for hashing.
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] secretBytes = Base64.getDecoder().decode(base64Secret);
			// Hashes a secret string (JWT_SECRET) using SHA-256 and stores the resulting
			// hash bytes.
			byte[] digestBytes = digest.digest(secretBytes);

			return Jwts.builder().issuedAt(currentDate).expiration(expiryDate) //
					// Signs the JWT using a secret key derived from the SHA-256 hash, ensuring its
					// authenticity and integrity.
					.signWith(Keys.hmacShaKeyFor(digestBytes))//
					.claim("subject", email) //
					.claim("role", role).compact();
		} catch (Exception e) {
			throw new CustomException("GENERATE_TOKEN_ERROR",e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public boolean validateToken(String token) {
		try {
			// Creates a MessageDigest object for the SHA-256 algorithm, used for hashing.
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] secretBytes = Base64.getDecoder().decode(base64Secret);
			// Hashes a secret string (JWT_SECRET) using SHA-256 and stores the resulting
			// hash bytes.
			byte[] digestBytes = digest.digest(secretBytes);
			Jwts.parser().verifyWith(Keys.hmacShaKeyFor(digestBytes)).build().parse(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getClaimFromToken(String token, String claimName) {
		try {
			// Creates a MessageDigest object for the SHA-256 algorithm, used for hashing.
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] secretBytes = Base64.getDecoder().decode(base64Secret);
			// Hashes a secret string (JWT_SECRET) using SHA-256 and stores the resulting
			// hash bytes.
			byte[] digestBytes = digest.digest(secretBytes);
			Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(digestBytes)).build().parseSignedClaims(token)
					.getPayload();

			return claims.get(claimName, String.class);
		} catch (Exception e) {
			return null;
		}
	}
}

package com.transf.api.error;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {
		Map<String, String> errorBody = Map.of("errorCode", ex.getErrorCode(), "message", ex.getMessage());
		return new ResponseEntity<>(errorBody, ex.getStatus());
	}

}

package com.otp.app.advise;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.otp.app.exception.CustomFoundException;
import com.otp.app.exception.CustomNotFoundException;
import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(CustomFoundException.class)
	public String handleCustomFoundException(CustomFoundException ex ) {
		log.info(ex.getMessage());
		return ex.getMessage();
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CustomNotFoundException.class)
	public String handleCustomNotFoundException(CustomNotFoundException ex) {
		log.info(ex.getMessage());
		return ex.getMessage();
	}
}

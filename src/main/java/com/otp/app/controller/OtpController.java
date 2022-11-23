package com.otp.app.controller;

import java.time.Instant;
import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otp.app.exception.OtpExceptionHandler;
import com.otp.app.model.OtpModel;
import com.otp.app.response.OtpResponse;
import com.otp.app.service.OtpService;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

	@Autowired
	private OtpService otpService;
	String result = "";

	@PostMapping("/login")
	public ResponseEntity<?> generateOtp(@Valid @RequestBody OtpModel model) {


		byte[] secret =  ( model.getEmail() + LocalDateTime.now()).getBytes();

		for (byte b : secret) {
			result = result.concat("" + b);
		}

		try {
			String otp = otpService.generateTOTP256(result, "30", "6");
			OtpResponse otpRes =  OtpResponse.builder().statusCode("200").MessageTypeId("1").otp(otp).TraceId(Instant.now().toEpochMilli()).message("Otp Generated successfully").build();
			return new ResponseEntity<>(otpRes, HttpStatus.OK);

		} catch (Exception e) {
			throw new OtpExceptionHandler("otp not generated") ;
		}

	}

	@PostMapping("/validateOtp")
	public ResponseEntity<?> validateOtpCode(@RequestBody String otp ) {
		return  this.otpService.validateOtp(""+otp);
	}




}

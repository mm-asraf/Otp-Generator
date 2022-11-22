package com.otp.app.controller;

import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.otp.app.model.OtpModel;
import com.otp.app.service.OtpService;

@RestController
public class OtpController {
	
	@Autowired
	private OtpService otpService;
	String result = "";
	
	@PostMapping("/generateOtp")
	public String generateOtp(@Valid @RequestBody OtpModel model) {
		
		 
		   byte[] secret =  ( model.getEmail() + LocalDateTime.now()).getBytes();
		   
		   for (byte b : secret) {
			result = result+b;
		}
		
		 String otp = otpService.generateTOTP256(result, "30", "6");
		return  otp;
	}
	
	

}

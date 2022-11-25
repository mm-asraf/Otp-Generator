package com.otp.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.otp.app.model.OtpModel;


@SpringBootTest
class OtpServiceTest {
	
	@Autowired
	private OtpService otpService;
	
	
	
	OtpModel model = OtpModel.builder().email("mahboobasraf@gmail.com").build();
	
	 @Test
	 @DisplayName("check data based on valid code characters")
	   void whenGenerateOtp_thenCheckSixDigit() {
	        String otp = otpService.generateTOTP256_1A(model);
	        assertEquals(6, otp.length());
	             
	    }
	 
	 @Test
	 @DisplayName("when validate return with success status code")
	 void whenOtpValidate_thenReturnWithSuccessMessage() {
		 ResponseEntity<?> res=otpService.validateOtp(otpService.generateTOTP256_1A(model));
		 assertEquals(HttpStatus.ACCEPTED,res.getStatusCode());
	 }

}

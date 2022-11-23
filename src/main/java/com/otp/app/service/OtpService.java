package com.otp.app.service;

import org.springframework.http.ResponseEntity;

public interface OtpService {
	
	
	public  String generateTOTP(String key,
			String time,
			String returnDigits,
			String crypto);

	public  String generateTOTP256(String key,
			String time,
			String returnDigits);

	public  byte[] hexStr2Bytes(String hex);

	public  byte[] hmac_sha(String crypto, byte[] keyBytes,byte[] text);
	
	public ResponseEntity<?> validateOtp(String string);

	
	
	
	
}

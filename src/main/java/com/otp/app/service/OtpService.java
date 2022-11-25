package com.otp.app.service;

import org.springframework.http.ResponseEntity;

import com.otp.app.model.OtpModel;

public interface OtpService {


	public  String generateTOTP(
			String key,
			String time,
			String returnDigits,
			String crypto);



	public  String generateTOTP256_1A(OtpModel model);

	public  byte[] hexStr2Bytes(String hex);

	public  byte[] hmac_sha(String crypto, byte[] keyBytes,byte[] text);

	public ResponseEntity<?> validateOtp(String string);







}

package com.otp.app.service.Impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otp.app.exception.OtpExceptionHandler;
import com.otp.app.response.OtpResponse;
import com.otp.app.response.OtpValidationSuccessResponseHandler;
import com.otp.app.service.OtpService;



@Service
public class ServiceImpl implements OtpService {


	private static final int[] DIGITS_POWER
	= {1,10,100,1000,10000,100000,1000000,10000000,100000000 };

	private static LocalDateTime durationTime = null;
	private String result = null;

	private ServiceImpl() {

	}

	public  String generateTOTP256(String key,
			String time,
			String returnDigits){
		return generateTOTP(key, time, returnDigits, "HmacSHA256");
	}


	public  String generateTOTP(String key,
			String time,
			String returnDigits,
			String crypto){
		int codeDigits = Integer.decode(returnDigits).intValue();


		while (time.length() < 16 )
			time = "0".concat(time);


		byte[] msg = hexStr2Bytes(time);
		byte[] k = hexStr2Bytes(key);
		byte[] hash = hmac_sha(crypto, k, msg);
		int offset = hash[hash.length - 1] & 0xf;


		int binary =
				((hash[offset] & 0x7f) << 24) |
				((hash[offset + 1] & 0xff) << 16) |
				((hash[offset + 2] & 0xff) << 8) |
				(hash[offset + 3] & 0xff);

		int otp = binary % DIGITS_POWER[codeDigits];

		result = Integer.toString(otp);
		durationTime = LocalDateTime.now().plusSeconds(30);
		while (result.length() < codeDigits) {
			result = "0".concat(result);
		}
		return result;
	}

	public  byte[] hexStr2Bytes(String hex){

		byte[] bArray = new BigInteger("10" + hex,16).toByteArray();


		byte[] ret = new byte[bArray.length - 1];
		for (int i = 0; i < ret.length; i++)
			ret[i] = bArray[i+1];
		return ret;
	}


	public  byte[] hmac_sha(String crypto, byte[] keyBytes,
			byte[] text){
		try {
			Mac hmac;
			hmac = Mac.getInstance(crypto);
			SecretKeySpec macKey =
					new SecretKeySpec(keyBytes, "RAW_SECRET");
			hmac.init(macKey);
			return hmac.doFinal(text);
		} catch (GeneralSecurityException gse) {
			throw new UndeclaredThrowableException(gse);
		}
	}

	@Override
	public ResponseEntity<?> validateOtp(String otp) {
		if(!otp.equals(result)) {
			return new ResponseEntity<>("Otp didn't matched...",HttpStatus.GATEWAY_TIMEOUT);
		}

		if(LocalDateTime.now().compareTo(durationTime) < 0) {
			if(otp.equals(result)) {
				OtpValidationSuccessResponseHandler res = OtpValidationSuccessResponseHandler.builder().statusCode("200").messageTypeId("1").traceId(Instant.now().toEpochMilli()).message("Successfully Validated").build();
				return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
			}else {
				throw new OtpExceptionHandler("Incorrect Otp pls Enter valid Otp");
			}
		}else {
			return new ResponseEntity<>("otp code expired please resend...", HttpStatus.GATEWAY_TIMEOUT);
		}

	}


}

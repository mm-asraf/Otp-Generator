package com.otp.app.service.Impl;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otp.app.exception.OtpExceptionHandler;
import com.otp.app.model.OtpModel;
import com.otp.app.response.OtpValidationSuccessResponseHandler;
import com.otp.app.service.OtpService;




/**
 * This services Implementatiion class contains different methods that   uses the JCE to provide the crypto algorithm.
 * HMAC computes a Hashed Message Authentication Code with the crypto hash algorithm as a parameter.
 * @param crypto: the crypto algorithm (HmacSHA1, HmacSHA256,HmacSHA512)                           
 * @param keyBytes: the bytes to use for the HMAC key
 * @param text: the message or text to be authenticated
 */

@Service
public class ServiceImpl implements OtpService {


	private static final int[] DIGITS_POWER = {1,10,100,1000,10000,100000,1000000,10000000,100000000 };
	private static LocalDateTime durationTime = null;
	private String result = null;

	private ServiceImpl() {

	}


	/**
	 * This method generates a TOTP value for the given
	 * set of parameters.
	 * @param object model: contains parameters that is  shared secret, HEX encoded,time and number of digits 
	 * @param generateTOTP: it will perform some internal operation and then return six digit number;
	 */
	public String generateTOTP256_1A(OtpModel model) {
		String res = "";
		byte[] sec = (model.getEmail() +  LocalTime.now()).getBytes();
		for (byte b : sec) {
			res = res.concat("" + b);
		}
		return generateTOTP(res, "30", "6", "HmacSHA256");
	}



	/**
	 * This method generates a TOTP value for the given set of parameters.
	 * @param key: the shared secret, HEX encoded
	 * @param time: a value that reflects a time
	 * @param returnDigits: number of digits to return
	 * @return: a numeric String in base 10 that includes
	 *              {@link truncationDigits} digits
	 */
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



	/**
	 * This method converts a HEX string to Byte[]
	 * @param hex: the HEX string
	 * @return: a byte array
	 */
	public  byte[] hexStr2Bytes(String hex){

		/**
		 * Adding one byte to get the right conversion
		 *  Values starting with "0" can be converted
		 */	
		byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

		byte[] ret = new byte[bArray.length - 1];
		for (int i = 0; i < ret.length; i++)
			ret[i] = bArray[i+1];
		return ret;
	}


	public  byte[] hmac_sha(String crypto, byte[] keyBytes,byte[] text){

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

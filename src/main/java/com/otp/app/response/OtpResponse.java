package com.otp.app.response;

import java.sql.Time;
import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OtpResponse {
	
	
	private String statusCode;
	private String otp;
	private long TraceId;
	private String message;
	private  String MessageTypeId;

}

package com.otp.app.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OtpErrorResponse {
	
	
	private String statusCode;
	private String messageTypeId;
	private long traceId;
	private String message;
}

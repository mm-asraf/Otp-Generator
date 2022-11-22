package com.otp.app.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseModel {

	private int statusCode;
	private String message;
}

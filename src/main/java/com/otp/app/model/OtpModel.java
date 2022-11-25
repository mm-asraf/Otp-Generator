package com.otp.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Entity
public class OtpModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;



	@Email
	@Column(unique=true)
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$",message = "email should not contain special character but valid email style will be fine")
	private String email;

}

package com.amigoWallet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

import lombok.Setter;

@Setter
@Getter
public class ResetPassword {
	
	@Email(message = "{customer.email.valid}")
	@NotBlank(message = "{customer.email.required}")
	private String email;

}

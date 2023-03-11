package com.amigoWallet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {
	
	@Email(message = "{customer.email.valid}")
	@NotBlank(message = "{customer.email.required}")
	private String email;
	@NotBlank(message = "{customer.password.required}")
	private String password;
	private String role;
}

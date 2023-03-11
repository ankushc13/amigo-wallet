package com.amigoWallet.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {
	@Email(message = "{customer.email.valid}")
	@NotBlank(message = "{customer.email.required}")
	private String email;
	@NotBlank(message = "{change.password.required}")
	private String currentPassword;
	@NotBlank(message = "{change.newPassword.required}")
	private String newPassword;
}

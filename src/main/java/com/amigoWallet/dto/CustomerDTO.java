package com.amigoWallet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
	private Integer customerId;
	@NotBlank(message = "{customer.name.required}")
	private String name;
	@Email(message = "{customer.email.valid}")
	@NotBlank(message = "{customer.email.required}")
	private String email;
	@NotBlank(message = "{customer.mobile.required}")
	@Pattern(regexp = "([0-9]{10})", message = "{customer.mobile.valid}")
	private String mobileNumber;
	@NotBlank(message = "{customer.password.required}")
	private String password;
	private String role = "customer";

	public CustomerDTO() {
	}

	public CustomerDTO(String name, String email, String mobileNumber, String password, String role) {
		this.name = name;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.role = role;
	}

}

package com.amigo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.amigo.validation.AmountValidation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WalletDTO {

	private Integer walletId;
	@Email(message = "{customer.email.valid}")
	@NotBlank(message = "{customer.email.required}")
	private String email;
	@AmountValidation
	private Double walletAmount;
	private Integer rewardPoint = 0;
	private Integer bankId;
	
	public WalletDTO() {
	}

	public WalletDTO(
			String email,Double walletAmount) {
		this.email = email;
		this.walletAmount = walletAmount;
	}
	
	
}

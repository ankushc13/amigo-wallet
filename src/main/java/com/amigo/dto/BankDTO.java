package com.amigo.dto;

import javax.validation.constraints.Pattern;

import com.amigo.validation.AmountValidation;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankDTO {

	private Integer bankId;
	@Pattern(regexp = "(^[a-zA-Z]{4}\\d{6,8}$)",message = "Bank code should start with 4 alphabets followed by 6 to 8 digits")
	private String bankCode;
	@Pattern(regexp = "(^\\d*$)",message = "Account number should only contain digits")
	private String accountNumber;
	@Pattern(regexp = "^[a-zA-Z ]*$",message = "Account holder’s name should contain only alphabets and spaces")
	private String holderName;
	@AmountValidation
	private Double amount;
	public BankDTO() {
	}
	public BankDTO(String bankCode,String accountNumber, String holderName, Double amount) {
		this.bankCode = bankCode;
		this.accountNumber = accountNumber;
		this.holderName = holderName;
		this.amount = amount;
	}
	
	
}

package com.amigo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {
	
	private Integer transactionId;
	private LocalDateTime transactionDateTime;
	private String info;
	private Double amount;
	private String status;
	private Integer walletId;
}

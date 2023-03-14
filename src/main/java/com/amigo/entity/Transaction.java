package com.amigo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="transaction_id")
	private Integer transactionId;
	@Column(name = "transaction_date_time", columnDefinition = "TIMESTAMP")
	private LocalDateTime transactionDateTime;
	private String info;
	private Double amount;
	private String status;
    @Column(name="wallet_id")
	private Integer walletId;
}

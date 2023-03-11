package com.amigoWallet.entity;

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
@Table(name = "wallet")
public class Wallet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallet_id")
	private Integer walletId;
	private String email;
	@Column(name = "wallet_amount")
	private Double walletAmount;
    @Column(name = "reward_point",columnDefinition = "integer default 0")
	private Integer rewardPoint = 0;
	@Column(name = "bank_id")
	private Integer bankId;
}

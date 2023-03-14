package com.amigo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "merchant_details")
public class MerchantDetails {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="merchant_id")
	private Integer merchantId;
	private String name;
    @Column(name="utility_type")
	private String utilityType;
	private String email;

}

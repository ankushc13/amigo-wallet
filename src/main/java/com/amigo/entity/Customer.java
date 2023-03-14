package com.amigo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "customer")
public class Customer {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="customer_id")
	private Integer customerId;
	private String name;
	@Column(unique=true)
	private String email;
	private String mobileNumber;
	private String password;
	private String role;
}

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
@Table(name = "offers")
public class Offers {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="offer_id")
	private Integer offerId;
    @Column(name="offer_code")
	private String offerCode;
    @Column(name="offer_amount")
	private Integer offerAmount;
}

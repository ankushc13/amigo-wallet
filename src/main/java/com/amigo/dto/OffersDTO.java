package com.amigo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffersDTO {
	
	Integer offerId;
	String offerCode;
	Integer offerAmount;
	public OffersDTO() {}

	public OffersDTO(String offerCode, Integer offerAmount) {
		this.offerCode = offerCode;
		this.offerAmount = offerAmount;
	}
	
	
	
}

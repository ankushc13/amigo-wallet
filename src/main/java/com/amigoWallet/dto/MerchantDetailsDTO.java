package com.amigoWallet.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MerchantDetailsDTO {
	
	private Integer merchantId;
	private String name;
	private String utilityType;
	private String email;
	
	public MerchantDetailsDTO() {
	}

	public MerchantDetailsDTO(String name, String utilityType, String email) {
		this.name = name;
		this.utilityType = utilityType;
		this.email = email;
	}
    

}

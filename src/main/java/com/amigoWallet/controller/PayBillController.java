package com.amigoWallet.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.amigoWallet.dto.Login;
import com.amigoWallet.dto.MerchantDetailsDTO;
import com.amigoWallet.dto.WalletDTO;
import com.amigoWallet.exceptions.CustomerException;
import com.amigoWallet.exceptions.ErrorMessage;
import com.amigoWallet.service.PayBillService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PayBillController {
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	PayBillService payBillService;
	
	@GetMapping(value = "/merchant/load")
	public ResponseEntity<List<MerchantDetailsDTO>> showMerchants(HttpServletRequest request) throws CustomerException{
		Login login = (Login) request.getSession().getAttribute("customer");
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException("you are not logged in");
		}
		
		return ResponseEntity.ok(payBillService.getMerchant());
	}
	
	@PostMapping(value = "/paybill") 
	public ResponseEntity<String> tranferToWallet(@RequestBody MerchantDetailsDTO merchantDetailsDTO,HttpServletRequest request) throws CustomerException, JsonProcessingException{
		Login login = (Login) request.getSession().getAttribute("customer");
		ObjectMapper objectMapper = new ObjectMapper();
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException("you are not logged in");
		}
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(objectMapper.writeValueAsString(payBillService.prepareWallet(merchantDetailsDTO)),headers);
			ResponseEntity<WalletDTO> response = restTemplate.
				      postForEntity("http://localhost:8080/wallet/paybill/"+login.getEmail(), entity, WalletDTO.class);
			
			return ResponseEntity.ok("bill successfully paid, your total reward point: "+response.getBody().getRewardPoint());
		} catch (HttpStatusCodeException exception) {
			throw new CustomerException((objectMapper.readValue(exception.getResponseBodyAsString(),ErrorMessage.class)).getMessage());
		}
	}
	

}

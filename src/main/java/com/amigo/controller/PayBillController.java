package com.amigo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.amigo.dto.Login;
import com.amigo.dto.MerchantDetailsDTO;
import com.amigo.dto.WalletDTO;
import com.amigo.exceptions.CustomerException;
import com.amigo.exceptions.ErrorMessage;
import com.amigo.service.PayBillService;
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
			HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(payBillService.prepareWallet(merchantDetailsDTO)),headers);
			ResponseEntity<WalletDTO> response = restTemplate.
				      postForEntity("http://localhost:8080/wallet/paybill/"+login.getEmail(), entity, WalletDTO.class);
			WalletDTO walletDTO = response.getBody();
			if(!ObjectUtils.isEmpty(walletDTO)) {
				return ResponseEntity.ok("bill successfully paid, your total reward point: "+walletDTO.getRewardPoint());

			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		catch (HttpStatusCodeException exception) {
			throw new CustomerException((objectMapper.readValue(exception.getResponseBodyAsString(),ErrorMessage.class)).getMessage());
		}
	}
	

}

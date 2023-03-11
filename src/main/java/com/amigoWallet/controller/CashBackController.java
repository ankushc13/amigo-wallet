package com.amigoWallet.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.amigoWallet.dto.Login;
import com.amigoWallet.dto.TransactionDTO;
import com.amigoWallet.exceptions.CustomerException;
import com.amigoWallet.exceptions.ErrorMessage;
import com.amigoWallet.service.CashBackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CashBackController {
	
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	CashBackService cashBackService;
	
	
	@GetMapping(value = "/cashback/{transactionId}/{offerCode}")
	public ResponseEntity<String> getCashBack(@PathVariable String transactionId,@PathVariable String offerCode, HttpServletRequest request) throws CustomerException, JsonMappingException, JsonProcessingException {
		Login login = (Login) request.getSession().getAttribute("customer");
		ObjectMapper objectMapper = new ObjectMapper();
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException("you are not logged in");
		}
		try {
			//get offer amount 
			ResponseEntity<Integer> offerAmount = restTemplate.
				      getForEntity("http://localhost:8080/offers/"+offerCode, Integer.class);
			
			//get transaction
			ResponseEntity<TransactionDTO> transaction = restTemplate.
				      getForEntity("http://localhost:8080//transaction/"+transactionId, TransactionDTO.class);
			double cashBackAmount = cashBackService.getCashBackAmount(transaction.getBody(), offerAmount.getBody());
			
			//add cash-back to wallet
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			restTemplate.postForEntity("http://localhost:8080//wallet/"+login.getEmail()+"/"+cashBackAmount, entity, String.class);
			
			return ResponseEntity.ok("congrats! cashback amount: "+cashBackAmount+" has been added to you wallet");
		
		} catch (HttpStatusCodeException exception) {
			throw new CustomerException((objectMapper.readValue(exception.getResponseBodyAsString(),ErrorMessage.class)).getMessage());
		}
	}
}

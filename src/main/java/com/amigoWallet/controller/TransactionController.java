package com.amigoWallet.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.amigoWallet.dto.Login;
import com.amigoWallet.dto.TransactionDTO;
import com.amigoWallet.exceptions.CustomerException;
import com.amigoWallet.exceptions.ErrorMessage;
import com.amigoWallet.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	@Autowired
	RestTemplate restTemplate;
	
	@PostMapping(value = "/transaction/add")
	public ResponseEntity<String> addWallet(@RequestBody TransactionDTO transactionDTO) throws CustomerException {
		transactionService.addTransaction(transactionDTO);
		return ResponseEntity.status(HttpStatus.OK).body("success");
	}
	@GetMapping(value = "/transaction/page/{pageNo}")
	public ResponseEntity<List<TransactionDTO>> viewTransaction(@PathVariable Integer pageNo,HttpServletRequest request) throws CustomerException, JsonMappingException, JsonProcessingException {
		Login login = (Login) request.getSession().getAttribute("customer");
		ObjectMapper objectMapper = new ObjectMapper();
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException("you are not logged in");
		}
		try {
			ResponseEntity<Integer> response = restTemplate.
				      getForEntity("http://localhost:8080/wallet/fetch-wallet-id/"+login.getEmail(), Integer.class);
			Integer walletId = response.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(transactionService.showTransaction(walletId,pageNo-1));
		} catch (HttpStatusCodeException exception) {
			throw new CustomerException((objectMapper.readValue(exception.getResponseBodyAsString(),ErrorMessage.class)).getMessage());
		}
	}
	
	@GetMapping(value = "/transaction/{transactionId}")
	public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Integer transactionId) throws CustomerException, JsonMappingException, JsonProcessingException {
		return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransaction(transactionId));

	}
	

}

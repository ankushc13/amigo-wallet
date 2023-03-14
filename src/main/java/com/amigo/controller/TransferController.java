package com.amigo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.amigo.dto.BankDTO;
import com.amigo.dto.Login;
import com.amigo.dto.WalletDTO;
import com.amigo.exceptions.CustomerException;
import com.amigo.exceptions.ErrorMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TransferController {

	@Autowired
	RestTemplate restTemplate;

	@PostMapping(value = "/tranfer/bank")
	public ResponseEntity<String> tranferToBank(@Valid @RequestBody BankDTO bankDTO, HttpServletRequest request)
			throws CustomerException, JsonProcessingException {
		Login login = (Login) request.getSession().getAttribute("customer");
		ObjectMapper objectMapper = new ObjectMapper();
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException("you are not logged in");
		}
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(bankDTO),headers);
			return restTemplate.
				      postForEntity("http://localhost:8080/wallet/tranfer-bank/"+login.getEmail(), entity, String.class);
		} catch (HttpStatusCodeException exception) {
			throw new CustomerException((objectMapper.readValue(exception.getResponseBodyAsString(),ErrorMessage.class)).getMessage());
		}
	}
	
	@PostMapping(value = "/tranfer/wallet")
	public ResponseEntity<String> tranferToWallet(@Valid @RequestBody WalletDTO walletDTO, HttpServletRequest request)
			throws CustomerException, JsonProcessingException {
		Login login = (Login) request.getSession().getAttribute("customer");
		ObjectMapper objectMapper = new ObjectMapper();
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException("you are not logged in");
		}
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(walletDTO),headers);
			return restTemplate.
				      postForEntity("http://localhost:8080/wallet/tranfer-wallet/"+login.getEmail(), entity, String.class);
		} catch (HttpStatusCodeException exception) {
			throw new CustomerException((objectMapper.readValue(exception.getResponseBodyAsString(),ErrorMessage.class)).getMessage());
		}
	}

}

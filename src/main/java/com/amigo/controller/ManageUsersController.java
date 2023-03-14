package com.amigo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.amigo.dto.ChangePassword;
import com.amigo.dto.CustomerDTO;
import com.amigo.dto.Login;
import com.amigo.dto.ResetPassword;
import com.amigo.exceptions.CustomerException;
import com.amigo.exceptions.ErrorMessage;
import com.amigo.service.ManageUsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ManageUsersController {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	ManageUsersService manageUsersService;

	@PostMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createUser(@Valid @RequestBody CustomerDTO customerDTO) throws JsonProcessingException, CustomerException {
		if (manageUsersService.exist(customerDTO.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exist");
		}
		manageUsersService.createCustomer(customerDTO);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// adding wallet for customer
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			restTemplate.postForEntity("http://localhost:8080//wallet/add/"+customerDTO.getEmail(), entity, String.class);
		} catch (HttpStatusCodeException exception) {
			throw new CustomerException((objectMapper.readValue(exception.getResponseBodyAsString(),ErrorMessage.class)).getMessage());
		}
		return ResponseEntity.ok("Success");

	}

	@PostMapping(value = "/customer/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> loginUser(@RequestBody Login login, HttpServletRequest request)
			throws CustomerException {
		// logout previous customer
		if (manageUsersService.login(login)) {
			request.getSession().setAttribute("customer", login);
			return ResponseEntity.ok().body("login success");
		}
		return ResponseEntity.internalServerError().body("login failed");
	}

	@PostMapping(value = "/customer/change-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePassword changePassword) {
		if (!manageUsersService.verifyEmailAndPassword(changePassword)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email or current passowrd is incorrect");
		}
		manageUsersService.changePassword(changePassword.getEmail(), changePassword.getNewPassword());
		return ResponseEntity.ok("Success");
	}

	@PostMapping(value = "/customer/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> changePassword(@Valid @RequestBody ResetPassword resetPassword) {
		if (!manageUsersService.exist(resetPassword.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is incorrect");
		}
		return ResponseEntity.ok("new password: " + manageUsersService.resetPassword(resetPassword));
	}
}

package com.amigoWallet.controller;

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
import org.springframework.web.client.RestTemplate;

import com.amigoWallet.dto.ChangePassword;
import com.amigoWallet.dto.CustomerDTO;
import com.amigoWallet.dto.Login;
import com.amigoWallet.dto.ResetPassword;
import com.amigoWallet.exceptions.CustomerException;
import com.amigoWallet.service.ManageUsersService;

@RestController
public class ManageUsersController {

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	ManageUsersService manageUsersService;

	@PostMapping(value = "/customer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createUser(@Valid @RequestBody CustomerDTO customerDTO) {
		if (manageUsersService.exist(customerDTO.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exist");
		}
		manageUsersService.createCustomer(customerDTO);
		try {
			// adding wallet for customer
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(headers);
			restTemplate.postForEntity("http://localhost:8080//wallet/add/"+customerDTO.getEmail(), entity, String.class);
		} catch (Exception e) {
			// TODO: handle exception
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

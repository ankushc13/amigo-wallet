package com.amigo.controller;

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

import com.amigo.dto.BankDTO;
import com.amigo.dto.Login;
import com.amigo.dto.WalletDTO;
import com.amigo.exceptions.CustomerException;
import com.amigo.service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class WalletController {
	
	@Autowired
	WalletService loadWalletService;
	private String customerKey = "customer";
	private String notLogged = "you are not logged in";
	private String success = "success";

	@PostMapping(value = "/wallet/add/{email}")
	public ResponseEntity<String> addWallet(@PathVariable String email) throws CustomerException {
		loadWalletService.createWallet(email);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
	
	@GetMapping(value = "/wallet/load")
	public ResponseEntity<WalletDTO> loadWallet(HttpServletRequest request) throws CustomerException {
		
		Login login = (Login) request.getSession().getAttribute(customerKey);
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException(notLogged);
		}
		return ResponseEntity.status(HttpStatus.OK).body(loadWalletService.loadWallet(login.getEmail()));
	}
	@PostMapping(value = "/wallet/add-bank") 
	public ResponseEntity<String> addBank(@RequestBody BankDTO bankDTO,HttpServletRequest request) throws CustomerException{
		Login login = (Login) request.getSession().getAttribute(customerKey);
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException(notLogged);
		}
		loadWalletService.addBank(bankDTO,login.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
	@PostMapping(value = "/wallet/topup/{amount}") 
	public ResponseEntity<String> walletTopUp(@PathVariable Double amount,HttpServletRequest request) throws CustomerException, JsonProcessingException{
		Login login = (Login) request.getSession().getAttribute(customerKey);
		if (ObjectUtils.isEmpty(login)) {
			throw new CustomerException(notLogged);
		}
		if(amount<100) {
			throw new CustomerException("minimum amount cannot be less than 100 USD");
		}
		loadWalletService.addMoney(login.getEmail(),amount);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
	@PostMapping(value = "/wallet/tranfer-bank/{email}") 
	public ResponseEntity<String> tranferToBank(@RequestBody BankDTO bankDTO,@PathVariable String email) throws CustomerException, JsonProcessingException{
		loadWalletService.tranferToBank(bankDTO,email);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
	@PostMapping(value = "/wallet/tranfer-wallet/{email}") 
	public ResponseEntity<String> tranferToWallet(@RequestBody WalletDTO walletDTO,@PathVariable String email) throws CustomerException, JsonProcessingException{
		loadWalletService.tranferToWallet(walletDTO,email,0);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
	@PostMapping(value = "/wallet/paybill/{email}") 
	public ResponseEntity<WalletDTO> payBill(@RequestBody WalletDTO walletDTO,@PathVariable String email) throws CustomerException, JsonProcessingException{
		return ResponseEntity.status(HttpStatus.OK).body(loadWalletService.payBill(walletDTO,email));
	}
	@GetMapping(value = "/wallet/fetch-wallet-id/{email}")
	public ResponseEntity<Integer> getWalletId(@PathVariable String email) throws CustomerException{
		return ResponseEntity.status(HttpStatus.OK).body(loadWalletService.getWalletId(email)); 
	}
	@PostMapping(value = "/wallet/{email}/{amount}") 
	public ResponseEntity<String> addCashBack(@PathVariable String email,@PathVariable Double amount) throws CustomerException, JsonProcessingException{
		loadWalletService.addCashBack(email,amount);
		return ResponseEntity.status(HttpStatus.OK).body(success);
	}
	
}

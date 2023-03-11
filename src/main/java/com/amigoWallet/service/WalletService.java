package com.amigoWallet.service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amigoWallet.dto.BankDTO;
import com.amigoWallet.dto.TransactionDTO;
import com.amigoWallet.dto.WalletDTO;
import com.amigoWallet.entity.Bank;
import com.amigoWallet.entity.Wallet;
import com.amigoWallet.exceptions.CustomerException;
import com.amigoWallet.repository.AddBankRepository;
import com.amigoWallet.repository.LoadWalletRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class WalletService {

	@Autowired
	LoadWalletRepository loadWalletRepository;
	@Autowired
	AddBankRepository addBankRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	RestTemplate restTemplate;
	private static final DecimalFormat decfor = new DecimalFormat("0.00");  


	public void createWallet(String email) throws CustomerException {
		// TODO Auto-generated method stub
		Optional<Wallet> walletOptional=loadWalletRepository.findByEmail(email);
		if(!walletOptional.isPresent()) {
			Wallet wallet = new Wallet();
			wallet.setEmail(email);
			wallet.setWalletAmount(0.0);
			loadWalletRepository.saveAndFlush(wallet);
		}
		else {
			throw new CustomerException("wallet already exist");
		}
	}
	
	
	public WalletDTO loadWallet(String email) throws CustomerException {
		// TODO Auto-generated method stub
		
		Optional<Wallet> walletOptional=loadWalletRepository.findByEmail(email);
		if(!walletOptional.isPresent()) {
			throw new CustomerException("no wallet found");
		}
		WalletDTO walletDTO = modelMapper.map(walletOptional.get(), WalletDTO.class);
		return walletDTO;
	}
		
	public void addBank(BankDTO bankDTO, String email) throws CustomerException {
		// TODO Auto-generated method stub
		Optional<Bank> bankOptional=addBankRepository
				.findByBankCodeAndAccountNumberAndHolderName(bankDTO.getBankCode(),bankDTO.getAccountNumber(),bankDTO.getHolderName());
		Bank bank;
		if(bankOptional.isPresent()) {
			bank = bankOptional.get();
		}
		else {
			bankDTO.setAmount(getRandomNumber(1000, 10000));
			bank = modelMapper.map(bankDTO, Bank.class);
		}
		addBankRepository.saveAndFlush(bank);
		//add bank id to wallet
		WalletDTO walletDTO = loadWallet(email);
		walletDTO.setBankId(bank.getBankId());
		Wallet wallet = modelMapper.map(walletDTO, Wallet.class);
		loadWalletRepository.saveAndFlush(wallet);
	}
	public void addMoney(String email, Double amount) throws CustomerException, JsonProcessingException {
		// TODO Auto-generated method stub
		Optional<Wallet> walletOptional=loadWalletRepository.findByEmail(email);
		if(!walletOptional.isPresent()) {
			throw new CustomerException("no wallet found");
		}
		Optional<Bank> bankOptional=addBankRepository.findById(walletOptional.get().getBankId());
		if(!bankOptional.isPresent()) {
			throw new CustomerException("no bank account found");
		}
		Bank bank = bankOptional.get();
		if(bank.getAmount()<amount) {
			throw new CustomerException("insufficent bank balance");
		}
		bank.setAmount(bank.getAmount()-amount);
		addBankRepository.save(bank);
		Wallet wallet = walletOptional.get();
		wallet.setWalletAmount(wallet.getWalletAmount()+amount);
		loadWalletRepository.save(wallet);
		addBankRepository.flush();
		loadWalletRepository.flush();

		createTransaction(wallet.getWalletId(),email,"credited",amount,bank.getAccountNumber(),"debited");
		
	}


	public void tranferToBank(BankDTO bankDTO, String email) throws CustomerException, JsonProcessingException {
		// TODO Auto-generated method stub
		Optional<Bank> bankOptional=addBankRepository
				.findByBankCodeAndAccountNumberAndHolderName(bankDTO.getBankCode(),bankDTO.getAccountNumber(),bankDTO.getHolderName());
		if(!bankOptional.isPresent()) {
			throw new CustomerException("bank code or account number or holder name is incorrect");
		}
		Optional<Wallet> walletOptional=loadWalletRepository.findByEmail(email);
		if(!walletOptional.isPresent()) {
			throw new CustomerException("no wallet found");
		}
		Wallet wallet = walletOptional.get();

		if(wallet.getWalletAmount()<bankDTO.getAmount()) {
			throw new CustomerException("insufficent wallet balance");
		}

		Bank bank = bankOptional.get();
		bank.setAmount(bank.getAmount()+bankDTO.getAmount());
		addBankRepository.save(bank);
		
		wallet.setWalletAmount(wallet.getWalletAmount()-bankDTO.getAmount());
		loadWalletRepository.save(wallet);
		
		addBankRepository.flush();
		loadWalletRepository.flush();
		
		createTransaction(wallet.getWalletId(),email,"debited",bankDTO.getAmount(),bank.getAccountNumber(),"credited");

	}
	public Wallet tranferToWallet(WalletDTO walletDTO, String email, Integer rewardPoint) throws CustomerException, JsonProcessingException {
		// TODO Auto-generated method stub
			Optional<Wallet> walletReciverOptional=loadWalletRepository.findByEmail(walletDTO.getEmail());
			if(!walletReciverOptional.isPresent()) {
				throw new CustomerException("given wallet email is incorrect");
			}
			Optional<Wallet> walletOptional=loadWalletRepository.findByEmail(email);
			if(!walletOptional.isPresent()) {
				throw new CustomerException("no wallet found for client");
			}
			Wallet wallet = walletOptional.get();

			if(wallet.getWalletAmount()<walletDTO.getWalletAmount()) {
				throw new CustomerException("insufficent wallet balance");
			}

			Wallet walletReciver = walletReciverOptional.get();
			walletReciver.setWalletAmount(walletReciver.getWalletAmount()+walletDTO.getWalletAmount());
			loadWalletRepository.save(walletReciver);
			
			wallet.setWalletAmount(wallet.getWalletAmount()-walletDTO.getWalletAmount());
			wallet.setRewardPoint(wallet.getRewardPoint()+rewardPoint);
			loadWalletRepository.save(wallet);
			loadWalletRepository.flush();
			
			createTransaction(wallet.getWalletId(),email,"debited",walletDTO.getWalletAmount(),walletReciver.getEmail(),"credited");
			createTransaction(walletReciver.getWalletId(),walletReciver.getEmail(),"credited",walletDTO.getWalletAmount(),wallet.getEmail(),"debited");

			return wallet;
	}
	
	public Integer getWalletId(String email) throws CustomerException {
		Optional<Wallet> walletOptional=loadWalletRepository.findByEmail(email);
		if(!walletOptional.isPresent()) {
			throw new CustomerException("given wallet email is incorrect");
		}
		return walletOptional.get().getWalletId();
	}
	
	
	private void createTransaction(Integer walletId, String email, String type1, Double amount, String holderId,
			String type2) throws JsonProcessingException {
		
	    LocalDateTime dateTimeNow = LocalDateTime.now();
		String info = String.format("wallet %s %s for %s on %s; %s %s", email,type1,amount,dateTimeNow,holderId,type2);
		
		TransactionDTO transactionDTO = new TransactionDTO();
		transactionDTO.setAmount(amount);
		transactionDTO.setInfo(info);
		transactionDTO.setStatus("success");
		transactionDTO.setWalletId(walletId);
		transactionDTO.setTransactionDateTime(LocalDateTime.now());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(objectMapper.writeValueAsString(transactionDTO),headers);
		restTemplate.postForEntity("http://localhost:8080/transaction/add", entity, String.class);
		
	}


	public String getEmail(Integer walletId) throws CustomerException {
		// TODO Auto-generated method stub
		Optional<Wallet> walletReciverOptional=loadWalletRepository.findById(walletId);
		if(!walletReciverOptional.isPresent()) {
			throw new CustomerException("given wallet id is incorrect");
		}
		return walletReciverOptional.get().getEmail();
	}


	public WalletDTO payBill(WalletDTO walletDTO, String emailId) throws JsonProcessingException, CustomerException {
		// TODO Auto-generated method stub
		Integer rewardPoint= calculateRewardPoints(walletDTO.getWalletAmount());
		Wallet wallet = tranferToWallet(walletDTO, emailId, rewardPoint);
		
		return modelMapper.map(wallet,WalletDTO.class);
	}
	public void addCashBack(String email, Double amount) throws CustomerException, JsonProcessingException {
		// TODO Auto-generated method stub
		Optional<Wallet> walletOptional=loadWalletRepository.findByEmail(email);
		if(!walletOptional.isPresent()) {
			throw new CustomerException("given wallet email is incorrect");
		}
		Wallet wallet = walletOptional.get();
		wallet.setWalletAmount(wallet.getWalletAmount()+amount);
		loadWalletRepository.saveAndFlush(wallet);
		
		createTransaction(getWalletId(email),email,"credited",amount,"","");
	}
	private double getRandomNumber(double min, double max) {
	    Random random = new Random();
	    double num = random.nextDouble()*(max - min) + min;
	    return Double.parseDouble(decfor.format(num));
	}

	private Integer calculateRewardPoints(Double walletAmount) {
		// TODO Auto-generated method stub
		return (int) (getRandomNumber(0.0,10.0)*(walletAmount/200.0));
	}
	
	
}

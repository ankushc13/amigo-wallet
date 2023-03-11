package com.amigoWallet.service;

import java.text.DecimalFormat;

import org.springframework.stereotype.Service;

import com.amigoWallet.dto.TransactionDTO;

@Service
public class CashBackService {
	
	private static final DecimalFormat decfor = new DecimalFormat("0.00");  

	public double getCashBackAmount(TransactionDTO transactionDTO,Integer cashBackPercantage) {
		double cashBackAmount = (transactionDTO.getAmount()*cashBackPercantage)/100;
		return Double.parseDouble(decfor.format(cashBackAmount));
	}

}

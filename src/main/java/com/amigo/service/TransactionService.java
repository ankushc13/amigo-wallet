package com.amigo.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.amigo.dto.TransactionDTO;
import com.amigo.entity.Transaction;
import com.amigo.exceptions.CustomerException;
import com.amigo.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	ModelMapper modelMapper;

	public void addTransaction(TransactionDTO transactionDTO) {
		Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
		transactionRepository.saveAndFlush(transaction);
	}

	public List<TransactionDTO> showTransaction(Integer walletId, Integer pageNo) {
		Page<Transaction> allTransactions = transactionRepository.findByWalletId(walletId,PageRequest.of(pageNo, 5));
		List<Transaction> transactionsList = allTransactions.getContent();
		
		Type listType = new TypeToken<List<TransactionDTO>>(){}.getType();
		return modelMapper.map(transactionsList,listType);
	}

	public TransactionDTO getTransaction(Integer transactionId) throws CustomerException {
		Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
		if(!transactionOptional.isPresent()) {
			throw new CustomerException("transaction id incorrect");
		}
		return modelMapper.map(transactionOptional.get(), TransactionDTO.class);
	}
	

}

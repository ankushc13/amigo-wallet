package com.amigoWallet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.amigoWallet.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer>{

	Page<Transaction> findByWalletId(Integer walletId, Pageable pageable);


}

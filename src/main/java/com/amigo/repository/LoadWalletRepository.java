package com.amigo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigo.entity.Wallet;

public interface LoadWalletRepository extends JpaRepository<Wallet, Integer> {

	boolean existsByEmail(String email);

	Optional<Wallet> findByEmail(String email);

	
}

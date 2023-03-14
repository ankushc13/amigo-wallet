package com.amigo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigo.entity.Customer;

public interface ManageUsersRepository extends JpaRepository<Customer, Integer> {

	boolean existsByEmail(String email);
	@Override
	List<Customer> findAll();

	Optional<Customer> findByEmail(String email);
}

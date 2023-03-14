package com.amigo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigo.entity.Offers;

public interface ManageOffersRepository extends JpaRepository<Offers, Integer> {

	List<Offers> findAll();

	Optional<Offers> findByOfferCode(String offerCode);

	boolean existsByOfferCode(String offerCode);
}

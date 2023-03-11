package com.amigoWallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amigoWallet.entity.MerchantDetails;

public interface MerchantRepository extends JpaRepository<MerchantDetails, Integer> {

	Optional<MerchantDetails> findByNameAndUtilityType(String name, String utilityType);


}

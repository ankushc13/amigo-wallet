package com.amigoWallet.service;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amigoWallet.dto.MerchantDetailsDTO;
import com.amigoWallet.dto.WalletDTO;
import com.amigoWallet.entity.MerchantDetails;
import com.amigoWallet.exceptions.CustomerException;
import com.amigoWallet.repository.MerchantRepository;

@Service
public class PayBillService {
	
	@Autowired
	MerchantRepository merchantRepository;
	@Autowired
	ModelMapper modelMapper;
	private static final DecimalFormat decfor = new DecimalFormat("0.00");  

	public WalletDTO prepareWallet(MerchantDetailsDTO merchantDetailsDTO) throws CustomerException {
		
		Optional<MerchantDetails> merchantOptional = merchantRepository.findByNameAndUtilityType(merchantDetailsDTO.getName(), merchantDetailsDTO.getUtilityType());
		if(!merchantOptional.isPresent()) {
			throw new CustomerException("merchant name or utility is incorrect");
		}
		WalletDTO walletDTO = new WalletDTO();
		walletDTO.setEmail(merchantOptional.get().getEmail());
		walletDTO.setWalletAmount(getRandomNumber(50, 200));
		return walletDTO;
	}
	private double getRandomNumber(double min, double max) {
	    Random random = new Random();
	    double num = random.nextDouble()*(max - min) + min;
	    return Double.parseDouble(decfor.format(num));
	}
	public List<MerchantDetailsDTO> getMerchant() {
		List<MerchantDetails> merchantDetails = merchantRepository.findAll();
		Type listType = new TypeToken<List<MerchantDetailsDTO>>(){}.getType();
		List<MerchantDetailsDTO> merchantDetailsDTOs = modelMapper.map(merchantDetails,listType);
		return merchantDetailsDTOs;
	}

}

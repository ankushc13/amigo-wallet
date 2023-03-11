package com.amigoWallet.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.TypeToken;
import java.lang.reflect.Type;
import com.amigoWallet.dto.OffersDTO;
import com.amigoWallet.entity.Offers;
import com.amigoWallet.repository.ManageOffersRepository;

@Service
public class ManageOffersService {
	
	@Autowired
	ManageOffersRepository manageOffersRepository;
	@Autowired
	ModelMapper modelMapper;
	
	public List<OffersDTO> showAll() {
		// TODO Auto-generated method stub
		List<Offers> offers = manageOffersRepository.findAll();
		Type listType = new TypeToken<List<OffersDTO>>(){}.getType();
		List<OffersDTO> OffersDTO = modelMapper.map(offers,listType);
		return OffersDTO;
	}
	
	public boolean exist(String offerCode){
	       return manageOffersRepository.existsByOfferCode(offerCode);
	    }

	public void addOffer(OffersDTO offersDTO) {
		Offers offers = modelMapper.map(offersDTO, Offers.class);
		System.out.println(offers.getOfferCode());
		System.out.println(offers.getOfferCode());
		manageOffersRepository.save(offers);
	}

	public Integer getOfferAmount(String offerCode) {
		// TODO Auto-generated method stub
		Optional<Offers> offerOptional = manageOffersRepository.findByOfferCode(offerCode);
		if(offerOptional.isPresent()) {
			return offerOptional.get().getOfferAmount();
		}
		return null;
	}


	
	
}

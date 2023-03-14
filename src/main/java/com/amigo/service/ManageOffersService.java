package com.amigo.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amigo.dto.OffersDTO;
import com.amigo.entity.Offers;
import com.amigo.repository.ManageOffersRepository;

@Service
public class ManageOffersService {
	
	@Autowired
	ManageOffersRepository manageOffersRepository;
	@Autowired
	ModelMapper modelMapper;
	
	public List<OffersDTO> showAll() {

		List<Offers> offers = manageOffersRepository.findAll();
		Type listType = new TypeToken<List<OffersDTO>>(){}.getType();
		return modelMapper.map(offers,listType);
	}
	
	public boolean exist(String offerCode){
	       return manageOffersRepository.existsByOfferCode(offerCode);
	    }

	public void addOffer(OffersDTO offersDTO) {
		Offers offers = modelMapper.map(offersDTO, Offers.class);
		manageOffersRepository.save(offers);
	}

	public Integer getOfferAmount(String offerCode) {

		Optional<Offers> offerOptional = manageOffersRepository.findByOfferCode(offerCode);
		if(offerOptional.isPresent()) {
			return offerOptional.get().getOfferAmount();
		}
		return null;
	}


	
	
}

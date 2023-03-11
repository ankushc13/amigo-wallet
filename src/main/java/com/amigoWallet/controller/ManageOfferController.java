package com.amigoWallet.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.amigoWallet.dto.Login;
import com.amigoWallet.dto.OffersDTO;
import com.amigoWallet.service.ManageOffersService;

@RestController
public class ManageOfferController {

	@Autowired
	ManageOffersService manageOffersService;

	@GetMapping(value = "/offers/all")
	public ResponseEntity<List<OffersDTO>> showAllOffers() {

		return ResponseEntity.status(HttpStatus.OK).body(manageOffersService.showAll());

	}

	@GetMapping(value = "/offers/{offerCode}")
	public ResponseEntity<Integer> getOffer(@PathVariable String offerCode) {

		return ResponseEntity.status(HttpStatus.OK).body(manageOffersService.getOfferAmount(offerCode));

	}

	@PostMapping(value = "/offers/add")
	public ResponseEntity<String> addOffer(@RequestBody OffersDTO offersDTO, HttpServletRequest request) {
		Login login = (Login) request.getSession().getAttribute("customer");
		System.out.println(login);
		if (ObjectUtils.isEmpty(login)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not logged in");
		}
		if (manageOffersService.exist(offersDTO.getOfferCode())) {
			ResponseEntity.status(HttpStatus.OK).body("offer already exist");
		}
		if (login.getRole().equalsIgnoreCase("admin")) {
			manageOffersService.addOffer(offersDTO);
			return ResponseEntity.status(HttpStatus.OK).body("Success");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("only admin has access");

	}

}

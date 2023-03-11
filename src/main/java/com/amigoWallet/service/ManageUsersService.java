package com.amigoWallet.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amigoWallet.dto.ChangePassword;
import com.amigoWallet.dto.CustomerDTO;
import com.amigoWallet.dto.Login;
import com.amigoWallet.dto.ResetPassword;
import com.amigoWallet.entity.Customer;
import com.amigoWallet.exceptions.CustomerException;
import com.amigoWallet.repository.ManageUsersRepository;

@Service
public class ManageUsersService {
	
	@Autowired
	ManageUsersRepository manageUsersRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	//sign up
	public void createCustomer(CustomerDTO customerDTO) {
		Customer customer = this.modelMapper.map(customerDTO, Customer.class);
		manageUsersRepository.save(customer);
	}
	
	// Login
	public boolean login(Login loginDTO) throws CustomerException {
		Optional<Customer> customerOptional=manageUsersRepository.findByEmail(loginDTO.getEmail());
		if(!customerOptional.isPresent()) {
			throw new CustomerException("email or password is incorrect");
		}
		else if(!customerOptional.get().getPassword().equals(loginDTO.getPassword())) {
			throw new CustomerException("email or password is incorrect");
		}
		Customer customer = customerOptional.get();
		loginDTO.setRole(customer.getRole());
		System.out.print("login success");
		return true;
	}

    public boolean exist(String email){
       return manageUsersRepository.existsByEmail(email);
    }

	public void changePassword(String email, String newPassword) {
		Optional<Customer> customerOptional=manageUsersRepository.findByEmail(email);
		if(customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customer.setPassword(newPassword);
			manageUsersRepository.save(customer);
		}
	}

	public boolean verifyEmailAndPassword(ChangePassword changePasswordDTO) {
		// TODO Auto-generated method stub
		Optional<Customer> customerOptional=manageUsersRepository.findByEmail(changePasswordDTO.getEmail());
		return customerOptional.isPresent() && customerOptional.get().getPassword().equals(changePasswordDTO.getCurrentPassword());
	}

	public String resetPassword(ResetPassword resetPassword) {
		// TODO Auto-generated method stub
		String defaultNewPasswordString = "password";
		changePassword(resetPassword.getEmail(), defaultNewPasswordString);
		
		return defaultNewPasswordString;

		
	}

}

package com.amigo;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.amigo.dto.BankDTO;
import com.amigo.dto.CustomerDTO;
import com.amigo.dto.MerchantDetailsDTO;
import com.amigo.dto.OffersDTO;
import com.amigo.dto.WalletDTO;
import com.amigo.entity.Bank;
import com.amigo.entity.Customer;
import com.amigo.entity.MerchantDetails;
import com.amigo.entity.Offers;
import com.amigo.entity.Wallet;
import com.amigo.repository.AddBankRepository;
import com.amigo.repository.LoadWalletRepository;
import com.amigo.repository.ManageOffersRepository;
import com.amigo.repository.ManageUsersRepository;
import com.amigo.repository.MerchantRepository;

@SpringBootApplication
public class AmigoWalletApplication implements CommandLineRunner {

	@Autowired
	ManageUsersRepository manageUsersRepository;
	@Autowired
	ManageOffersRepository manageOffersRepository;
	@Autowired
	AddBankRepository addBankRepository;
	@Autowired
	LoadWalletRepository loadWalletRepository;
	@Autowired
	MerchantRepository merchantRepository;
	private String password = "12345";
	private String adminRole = "admin";
	private String merchantRole = "merchant";
	private String email1 = "jio@abc.com";
	private String email2 = "tata@abc.com";
	private String email3 = "gov@raj.com";



	public static void main(String[] args) {
		SpringApplication.run(AmigoWalletApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (manageUsersRepository.findAll().isEmpty()) {
			addCustomer();
		}
		if (manageOffersRepository.findAll().isEmpty()) {
			addOffer();
		}
		if (addBankRepository.findAll().isEmpty()) {
			addBank();
		}
		if (loadWalletRepository.findAll().isEmpty()) {
			addWallet();
		}
		if (merchantRepository.findAll().isEmpty()) {
			addMerchant();
		}
	}


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.setConnectTimeout(Duration.ofMillis(60000)).setReadTimeout(Duration.ofMillis(60000)).build();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	private void addCustomer(){
		// add admins if not exist
		CustomerDTO customer1 = new CustomerDTO("Ankush", "abc@abc.com", "9876543210",password , adminRole);
		CustomerDTO customer2 = new CustomerDTO("Messi", "def@abc.com", "9876543211", password, adminRole);
		CustomerDTO customer3 = new CustomerDTO("Chauhan", "ghi@abc.com", "9876543212", password, adminRole);

		// add merchant if not exist
		CustomerDTO customer4 = new CustomerDTO("JIO", email1, "9876543210", password, merchantRole);
		CustomerDTO customer5 = new CustomerDTO("TATA SKY", email2, "9876543211", password, merchantRole);
		CustomerDTO customer6 = new CustomerDTO("Public Health Engineering", email3, "9876543212", password,merchantRole);
		manageUsersRepository.save(modelMapper().map(customer1, Customer.class));
		manageUsersRepository.save(modelMapper().map(customer2, Customer.class));
		manageUsersRepository.save(modelMapper().map(customer3, Customer.class));
		manageUsersRepository.save(modelMapper().map(customer4, Customer.class));
		manageUsersRepository.save(modelMapper().map(customer5, Customer.class));
		manageUsersRepository.save(modelMapper().map(customer6, Customer.class));
	}

	private void addOffer(){
		// add offers if not exist
		OffersDTO offersDTO1 = new OffersDTO("ABC10", 10);
		OffersDTO offersDTO2 = new OffersDTO("ABC30", 30);
		OffersDTO offersDTO3 = new OffersDTO("ABC50", 50);
		
		manageOffersRepository.save(modelMapper().map(offersDTO1, Offers.class));
		manageOffersRepository.save(modelMapper().map(offersDTO2, Offers.class));
		manageOffersRepository.save(modelMapper().map(offersDTO3, Offers.class));	
	}

	private void addBank(){
		// add offers if not exist
		BankDTO bankDTO1 = new BankDTO("ABCD123456", "123456", "Ankush", 1000d);
		BankDTO bankDTO2 = new BankDTO("ABCD123789", "456456", "Messi", 2000d);
		BankDTO bankDTO3 = new BankDTO("ABCD123321", "789456", "Chauhan", 3000d);

		addBankRepository.save(modelMapper().map(bankDTO1, Bank.class));
		addBankRepository.save(modelMapper().map(bankDTO2, Bank.class));
		addBankRepository.save(modelMapper().map(bankDTO3, Bank.class));
	}

	private void addWallet(){
		// add wallets if not exist
		WalletDTO walletDTO1 = new WalletDTO("abc@abc.com", 200d);
		WalletDTO walletDTO2 = new WalletDTO("def@abc.com", 300d);
		WalletDTO walletDTO3 = new WalletDTO("ghi@abc.com", 250d);

		// add merchant wallet if not exist
		WalletDTO walletDTO4 = new WalletDTO(email1, 10000d);
		WalletDTO walletDTO5 = new WalletDTO(email2, 10000d);
		WalletDTO walletDTO6 = new WalletDTO(email3, 10000d);

		loadWalletRepository.save(modelMapper().map(walletDTO1, Wallet.class));
		loadWalletRepository.save(modelMapper().map(walletDTO2, Wallet.class));
		loadWalletRepository.save(modelMapper().map(walletDTO3, Wallet.class));
		loadWalletRepository.save(modelMapper().map(walletDTO4, Wallet.class));
		loadWalletRepository.save(modelMapper().map(walletDTO5, Wallet.class));
		loadWalletRepository.save(modelMapper().map(walletDTO6, Wallet.class));
	}
	
	private void addMerchant(){
		MerchantDetailsDTO merchantDetailsDTO1 = new MerchantDetailsDTO("JIO","mobile bill payment",email1);
		MerchantDetailsDTO merchantDetailsDTO2 = new MerchantDetailsDTO("TATA SKY","tv bill payment",email2);
		MerchantDetailsDTO merchantDetailsDTO3 = new MerchantDetailsDTO("Public Health Engineering","water bill payment",email3);
		merchantRepository.save(modelMapper().map(merchantDetailsDTO1, MerchantDetails.class));
		merchantRepository.save(modelMapper().map(merchantDetailsDTO2, MerchantDetails.class));
		merchantRepository.save(modelMapper().map(merchantDetailsDTO3, MerchantDetails.class));
	}

}

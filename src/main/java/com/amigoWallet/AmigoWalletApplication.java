package com.amigoWallet;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.amigoWallet.dto.BankDTO;
import com.amigoWallet.dto.CustomerDTO;
import com.amigoWallet.dto.MerchantDetailsDTO;
import com.amigoWallet.dto.OffersDTO;
import com.amigoWallet.dto.WalletDTO;
import com.amigoWallet.entity.Bank;
import com.amigoWallet.entity.Customer;
import com.amigoWallet.entity.MerchantDetails;
import com.amigoWallet.entity.Offers;
import com.amigoWallet.entity.Wallet;
import com.amigoWallet.repository.AddBankRepository;
import com.amigoWallet.repository.LoadWalletRepository;
import com.amigoWallet.repository.ManageOffersRepository;
import com.amigoWallet.repository.ManageUsersRepository;
import com.amigoWallet.repository.MerchantRepository;

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

	private void addCustomer() throws Exception {
		// add admins if not exist
		CustomerDTO customer1 = new CustomerDTO("Ankush", "abc@abc.com", "9876543210", "12345", "admin");
		CustomerDTO customer2 = new CustomerDTO("Messi", "def@abc.com", "9876543211", "12345", "admin");
		CustomerDTO customer3 = new CustomerDTO("Chauhan", "ghi@abc.com", "9876543212", "12345", "admin");

		// add merchant if not exist
		CustomerDTO customer4 = new CustomerDTO("JIO", "jio@abc.com", "9876543210", "12345", "merchant");
		CustomerDTO customer5 = new CustomerDTO("TATA SKY", "tata@abc.com", "9876543211", "12345", "merchant");
		CustomerDTO customer6 = new CustomerDTO("Public Health Engineering", "gov@raj.com", "9876543212", "12345","merchant");
		try {
			manageUsersRepository.save(modelMapper().map(customer1, Customer.class));
			manageUsersRepository.save(modelMapper().map(customer2, Customer.class));
			manageUsersRepository.save(modelMapper().map(customer3, Customer.class));
			manageUsersRepository.save(modelMapper().map(customer4, Customer.class));
			manageUsersRepository.save(modelMapper().map(customer5, Customer.class));
			manageUsersRepository.save(modelMapper().map(customer6, Customer.class));
		} catch (Exception e) {
			throw new Exception("admin already added");
		}
	}

	private void addOffer() throws Exception {
		// add offers if not exist
		OffersDTO offersDTO1 = new OffersDTO("ABC10", 10);
		OffersDTO offersDTO2 = new OffersDTO("ABC30", 30);
		OffersDTO offersDTO3 = new OffersDTO("ABC50", 50);

		try {
			manageOffersRepository.save(modelMapper().map(offersDTO1, Offers.class));
			manageOffersRepository.save(modelMapper().map(offersDTO2, Offers.class));
			manageOffersRepository.save(modelMapper().map(offersDTO3, Offers.class));
		} catch (Exception e) {
			throw new Exception("offer already added");
		}
	}

	private void addBank() throws Exception {
		// add offers if not exist
		BankDTO bankDTO1 = new BankDTO("ABCD123456", "123456", "Ankush", 1000d);
		BankDTO bankDTO2 = new BankDTO("ABCD123789", "456456", "Messi", 2000d);
		BankDTO bankDTO3 = new BankDTO("ABCD123321", "789456", "Chauhan", 3000d);

		try {
			addBankRepository.save(modelMapper().map(bankDTO1, Bank.class));
			addBankRepository.save(modelMapper().map(bankDTO2, Bank.class));
			addBankRepository.save(modelMapper().map(bankDTO3, Bank.class));
		} catch (Exception e) {
			throw new Exception("bank already added");
		}
	}

	private void addWallet() throws Exception {
		// add wallets if not exist
		WalletDTO walletDTO1 = new WalletDTO("abc@abc.com", 200d);
		WalletDTO walletDTO2 = new WalletDTO("def@abc.com", 300d);
		WalletDTO walletDTO3 = new WalletDTO("ghi@abc.com", 250d);

		// add merchant wallet if not exist
		WalletDTO walletDTO4 = new WalletDTO("jio@abc.com", 10000d);
		WalletDTO walletDTO5 = new WalletDTO("tata@abc.com", 10000d);
		WalletDTO walletDTO6 = new WalletDTO("gov@raj.com", 10000d);

		try {
			loadWalletRepository.save(modelMapper().map(walletDTO1, Wallet.class));
			loadWalletRepository.save(modelMapper().map(walletDTO2, Wallet.class));
			loadWalletRepository.save(modelMapper().map(walletDTO3, Wallet.class));
			loadWalletRepository.save(modelMapper().map(walletDTO4, Wallet.class));
			loadWalletRepository.save(modelMapper().map(walletDTO5, Wallet.class));
			loadWalletRepository.save(modelMapper().map(walletDTO6, Wallet.class));
		} catch (Exception e) {
			throw new Exception("wallet already added");
		}
	}
	
	private void addMerchant() throws Exception {
		// TODO Auto-generated method stub
		MerchantDetailsDTO merchantDetailsDTO1 = new MerchantDetailsDTO("JIO","mobile bill payment","jio@abc.com");
		MerchantDetailsDTO merchantDetailsDTO2 = new MerchantDetailsDTO("TATA SKY","tv bill payment","tata@abc.com");
		MerchantDetailsDTO merchantDetailsDTO3 = new MerchantDetailsDTO("Public Health Engineering","water bill payment","gov@raj.com");
		try {
			merchantRepository.save(modelMapper().map(merchantDetailsDTO1, MerchantDetails.class));
			merchantRepository.save(modelMapper().map(merchantDetailsDTO2, MerchantDetails.class));
			merchantRepository.save(modelMapper().map(merchantDetailsDTO3, MerchantDetails.class));

		} catch (Exception e) {
			throw new Exception("merchant already added");
		}
	}

}

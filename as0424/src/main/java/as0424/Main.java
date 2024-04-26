package main.java.as0424;

import java.time.LocalDate;

import main.java.as0424.exceptions.CheckoutException;
import main.java.as0424.pojo.checkout.Checkout;
import main.java.as0424.pojo.checkout.RentalAgreement;
import main.java.as0424.pojo.tool.Chainsaw;
import main.java.as0424.service.RentalService;

public class Main {

	public static void main(String[] args) {
		String toolCode = "CHNS";
		String brand = "Stihl";
		LocalDate checkoutDate = LocalDate.now();
		RentalService rentalService = new RentalService();
		
		try {
			Checkout checkout = new Checkout(toolCode, 5, 20, checkoutDate);
			RentalAgreement rentalAgreement = rentalService.generateAgreement(new Chainsaw(toolCode, brand), checkout);
			
			rentalAgreement.printAgreement();
		} catch (CheckoutException ex) {
			ex.printStackTrace();
		}
	}

}

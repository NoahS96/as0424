package main.java.as0424;

import main.java.as0424.pojo.checkout.Checkout;
import main.java.as0424.pojo.checkout.RentalAgreement;
import main.java.as0424.pojo.tool.Chainsaw;

public class Main {

	public static void main(String[] args) {
		String toolCode = "CHNS";
		String brand = "Stihl";
		
		Checkout checkout = new Checkout(toolCode, 5, 20);
		RentalAgreement rentalAgreement = new RentalAgreement(new Chainsaw(toolCode, brand), checkout);
		
		rentalAgreement.printAgreement();
	}

}

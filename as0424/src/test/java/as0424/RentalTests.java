package test.java.as0424;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import main.java.as0424.exceptions.CheckoutException;
import main.java.as0424.pojo.checkout.Checkout;
import main.java.as0424.pojo.checkout.RentalAgreement;
import main.java.as0424.pojo.tool.Chainsaw;
import main.java.as0424.pojo.tool.Jackhammer;
import main.java.as0424.pojo.tool.Ladder;
import main.java.as0424.service.RentalService;

class RentalTests {

	private static final String RIDGID_JACKHAMMER = "JAKR";
	private static final String RIDGID_BRAND = "Ridgid";
	private static final String DEWALT_JACKHAMMER = "JAKD";
	private static final String DEWALT_BRAND = "DeWalt";
	private static final String WERNER_LADDER = "LADW";
	private static final String WERNER_BRAND = "Werner";
	private static final String STIHL_CHAINSAW = "CHNS";
	private static final String STIHL_BRAND = "Stihl";
	
	private static final RentalService rentalService = new RentalService();
	
	
	@Test
	void discountTooLargeTest() {
		String toolCode = RIDGID_JACKHAMMER; 
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		
		Throwable exception = assertThrows(
			CheckoutException.class,
			() -> {
				new Checkout(toolCode, 5, 101, checkoutDate);
			}
		);
		
		assertEquals("Discount percent is not in the range 0-100", exception.getMessage());
	}

	@Test
	void julyFourthHolidayTest() {
		String toolCode = WERNER_LADDER;
		String toolBrand = WERNER_BRAND;
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		
		try {
			Checkout checkout = new Checkout(toolCode, 3, 10, checkoutDate);
			RentalAgreement rentalAgreement = rentalService.generateAgreement(new Ladder(toolCode, toolBrand), checkout);
			System.out.println("[Non-Chargeable Holiday Test]");
			rentalAgreement.printAgreement();
			
			assertEquals(2, rentalAgreement.getChargeableDays(), "Rental agreement incorrectly charged on 4th of July holiday");
			assertEquals("0.30", rentalAgreement.getDiscountAmount().toString(), "Discount amount is incorrect");
		} catch (CheckoutException ex) {
			fail("Exception thrown when generating rental agreement for July 4th holiday test");
		}
	} 
	
	@Test
	void chargeableJulyFourthHolidayTest() {
		String toolCode = STIHL_CHAINSAW;
		String toolBrand = STIHL_BRAND;
		LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
		
		try {
			Checkout checkout = new Checkout(toolCode, 5, 25, checkoutDate);
			RentalAgreement rentalAgreement = rentalService.generateAgreement(new Chainsaw(toolCode, toolBrand), checkout);
			System.out.println("[Chargeable Holiday and Weekday Test]");
			rentalAgreement.printAgreement();
			
			assertEquals(3, rentalAgreement.getChargeableDays(), "Rental agreement did not charge on 4th of July holiday");
			assertEquals("1.49", rentalAgreement.getDiscountAmount().toString(), "Discount amount is incorrect");
		} catch (CheckoutException ex) {
			fail("Exception thrown when generating rental agreement for chargeable July 4th holiday");
		}
	}
	
	@Test
	void dewaltJackhammerlaborDayHolidayTest() {
		String toolCode = DEWALT_JACKHAMMER;
		String toolBrand = DEWALT_BRAND;
		LocalDate checkoutDate = LocalDate.of(2015, 9, 3);
		
		try {
			Checkout checkout = new Checkout(toolCode, 6, 0, checkoutDate);
			RentalAgreement rentalAgreement = rentalService.generateAgreement(new Jackhammer(toolCode, toolBrand), checkout);
			System.out.println("[Non-Chargeable Holiday and Weekend Test]");
			rentalAgreement.printAgreement();
			
			assertEquals(3, rentalAgreement.getChargeableDays(), "Rental agreement incorrectly charged on Labor Day holiday");
			assertEquals("8.97", rentalAgreement.getFinalCharge().toString(), "Final charge amount is incorrect");
		} catch (CheckoutException ex) {
			fail("Exception thrown when generating rental agreement for Labor Day test");
		}
	}
	
	@Test
	void rigdigJackhammerJulyFourthHolidayTest() {
		String toolCode = RIDGID_JACKHAMMER;
		String toolBrand = RIDGID_BRAND;
		LocalDate checkoutDate = LocalDate.of(2015, 7, 2);
		
		try {
			Checkout checkout = new Checkout(toolCode, 9, 0, checkoutDate);
			RentalAgreement rentalAgreement = rentalService.generateAgreement(new Jackhammer(toolCode, toolBrand), checkout);
			System.out.println("[Non-Chargeable Holiday and Weekend Test 2]");
			rentalAgreement.printAgreement();
			
			assertEquals(6, rentalAgreement.getChargeableDays(), "Rental agreement incorrectly charged on Labor Day holiday");
			assertEquals("17.94", rentalAgreement.getFinalCharge().toString(), "Final charge amount is incorrect");
		} catch (CheckoutException ex) {
			fail("Exception thrown when generating rental agreement for Labor Day test");
		}
	}
	
	@Test
	void rigdigJackhammerDiscountJulyFourthHolidayTest() {
		String toolCode = RIDGID_JACKHAMMER;
		String toolBrand = RIDGID_BRAND;
		LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
		
		try {
			Checkout checkout = new Checkout(toolCode, 4, 50, checkoutDate);
			RentalAgreement rentalAgreement = rentalService.generateAgreement(new Jackhammer(toolCode, toolBrand), checkout);
			System.out.println("[Discount with Non-Chargeable Holiday and Weekend Test]");
			rentalAgreement.printAgreement();
			
			assertEquals(1, rentalAgreement.getChargeableDays(), "Rental agreement incorrectly charged on July 4th holiday");
			assertEquals("1.49", rentalAgreement.getFinalCharge().toString(), "Final charge amount is incorrect");
		} catch (CheckoutException ex) {
			fail("Exception thrown when generating rental agreement for Labor Day test");
		}
	}
}

package com.parabank.tests;

import com.parabank.tests.pages.BillPayPage;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillPaymentTest extends BaseTest {

    private BillPayPage billPayPage;

    @BeforeEach
    public void goToBillPay() {
        login("navaneeth", "demo");  // login first
        billPayPage = new BillPayPage(driver);
        billPayPage.open();     // then open bill pay
    }

    // ── TC-001 ─────────────────────────────────────────────────────
    @Test
    @Order(1)
    @DisplayName("TC-001: Successful payment with valid payee details")
    public void testSuccessfulBillPayment() {
        billPayPage.fillValidForm("50.00");
        billPayPage.clickSend();

        String result = billPayPage.getSuccessMessage();

        assertTrue(result.contains("Bill Payment Complete"),
                "Expected success message but got: " + result);
        assertTrue(result.contains("50.00"),
                "Amount 50.00 should appear in confirmation");
    }

    // ── TC-002 ─────────────────────────────────────────────────────
    @Test
    @Order(2)
    @DisplayName("TC-002/003/004/005: Submit with required fields empty")
    public void testEmptyRequiredFields() {
        // Click Send without filling anything
        billPayPage.clickSend();

        // Assert error messages appear for Name, Address, City
        String nameError    = billPayPage.getFieldError("validationModel-name");
        String addressError = billPayPage.getFieldError("validationModel-address");
        String cityError    = billPayPage.getFieldError("validationModel-city");

        assertFalse(nameError.isEmpty(),
                "Name validation error should be displayed");
        assertFalse(addressError.isEmpty(),
                "Address validation error should be displayed");
        assertFalse(cityError.isEmpty(),
                "City validation error should be displayed");
    }

    // ── TC-003 ─────────────────────────────────────────────────────
    @Test
    @Order(3)
    @DisplayName("TC-003: Validation failure when account numbers do not match")
    public void testMismatchedAccountNumbers() {
        billPayPage.fillValidForm("50.00");
        // deliberately mismatch verifyAccount
        driver.findElement(org.openqa.selenium.By.name("verifyAccount")).clear();
        driver.findElement(org.openqa.selenium.By.name("verifyAccount")).sendKeys("99999");
        billPayPage.clickSend();

        assertTrue(billPayPage.isAccountMismatchErrorDisplayed(),
                "Account numbers mismatch error should be displayed");
    }

    // ── TC-004 ─────────────────────────────────────────────────────
    @Test
    @Order(4)
    @DisplayName("TC-004: Bill payment with zero amount is accepted and confirmed")
    public void testZeroAmountPayment() {
        // Fill all fields with valid data but set amount to 0
        billPayPage.fillValidForm("0");
        billPayPage.clickSend();

        // ParaBank does not block zero-amount payments at the UI level;
        // it processes the payment and shows a "Bill Payment Complete" confirmation.
        String result = billPayPage.getSuccessMessage();

        assertTrue(result.contains("Bill Payment Complete"),
                "Expected Bill Payment Complete confirmation for zero-amount payment but got: " + result);
        assertTrue(result.contains("0"),
                "Confirmation should reference the submitted amount (0) but got: " + result);
    }
}

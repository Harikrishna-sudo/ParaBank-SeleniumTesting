package com.parabank.tests;

import com.parabank.tests.pages.ForgotLoginInfoPage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ForgotLoginInfoTest extends BaseTest {

    private ForgotLoginInfoPage forgotLoginPage;

    @BeforeEach
    public void setupPage() {
        forgotLoginPage = new ForgotLoginInfoPage(driver);
    }

    private String[] registerUniqueUser() {
        long timestamp = System.currentTimeMillis();
        String firstName = "TestUser";
        String lastName  = "Lookup" + timestamp;
        String street    = "123 Test St";
        String city      = "Dallas";
        String state     = "TX";
        String zipCode   = "75001";
        String phone     = "1234567890";
        String ssn       = "123-45-" + (timestamp % 10000);
        String username  = "user_" + timestamp;
        String password  = "pass_" + timestamp;

        driver.get(BASE_URL + "/register.htm");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer.firstName"))).sendKeys(firstName);
        driver.findElement(By.id("customer.lastName")).sendKeys(lastName);
        driver.findElement(By.id("customer.address.street")).sendKeys(street);
        driver.findElement(By.id("customer.address.city")).sendKeys(city);
        driver.findElement(By.id("customer.address.state")).sendKeys(state);
        driver.findElement(By.id("customer.address.zipCode")).sendKeys(zipCode);
        driver.findElement(By.id("customer.phoneNumber")).sendKeys(phone);
        driver.findElement(By.id("customer.ssn")).sendKeys(ssn);
        driver.findElement(By.id("customer.username")).sendKeys(username);
        driver.findElement(By.id("customer.password")).sendKeys(password);
        driver.findElement(By.id("repeatedPassword")).sendKeys(password);
        driver.findElement(By.cssSelector("input[value='Register']")).click();

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Log Out"))).click();
        } catch (Exception ignored) {
        }

        return new String[]{firstName, lastName, street, city, state, zipCode, phone, ssn, username, password};
    }

    // ── TC-001 ─────────────────────────────────────────────────────
    @Test
    @Order(1)
    @DisplayName("TC-001: Successfully retrieve forgotten username by supplying valid identity information")
    public void testRetrieveForgottenUsernameWithValidIdentity() {
        String[] user = registerUniqueUser();
        String firstName = user[0], lastName = user[1], street = user[2],
               city = user[3], state = user[4], zipCode = user[5],
               ssn = user[7], username = user[8];

        // Navigate to the "Forgot login info?" lookup page
        forgotLoginPage.open(BASE_URL);

        // Fill lookup form with valid personal identity information
        forgotLoginPage.fillLookupForm(firstName, lastName, street, city, state, zipCode, ssn);
        forgotLoginPage.clickFindMyLoginInfo();

        // Verify successful lookup result contains the username
        String resultText = forgotLoginPage.getResultText();
        assertTrue(resultText.contains("Your login information was located") || resultText.contains("Customer Lookup"),
                "Lookup result confirmation should be displayed. Got: " + resultText);
        assertTrue(resultText.contains(username),
                "Expected retrieved result to contain username '" + username + "', but got: " + resultText);
    }

    // ── TC-002 ─────────────────────────────────────────────────────
    @Test
    @Order(2)
    @DisplayName("TC-002: Successfully reset/retrieve password by supplying valid identity and username information")
    public void testRetrievePasswordWithValidIdentityAndUsername() {
        String[] user = registerUniqueUser();
        String firstName = user[0], lastName = user[1], street = user[2],
               city = user[3], state = user[4], zipCode = user[5],
               ssn = user[7], username = user[8], password = user[9];

        // Navigate to lookup page
        forgotLoginPage.open(BASE_URL);

        // Submit lookup with valid customer identity details
        forgotLoginPage.fillLookupForm(firstName, lastName, street, city, state, zipCode, ssn);
        forgotLoginPage.clickFindMyLoginInfo();

        // Verify that lookup returns both username and password details
        String resultText = forgotLoginPage.getResultText();
        assertTrue(resultText.contains("Your login information was located") || resultText.contains("Customer Lookup"),
                "Lookup should complete successfully. Got: " + resultText);
        assertTrue(resultText.contains(username),
                "Expected retrieved result to contain username '" + username + "', but got: " + resultText);
        assertTrue(resultText.contains(password),
                "Expected retrieved result to contain password '" + password + "', but got: " + resultText);
    }

    // ── TC-003 ─────────────────────────────────────────────────────
    @Test
    @Order(3)
    @DisplayName("TC-003: Submit the lookup form with identity information that does not match any customer record")
    public void testLookupWithNonMatchingIdentityInformation() {
        // Navigate to lookup page
        forgotLoginPage.open(BASE_URL);

        // Supply non-existent customer details
        String nonExistentFirst  = "NonExistent" + System.currentTimeMillis();
        String nonExistentLast   = "NotFound";
        String nonExistentStreet = "999 Unknown Blvd";
        String nonExistentCity   = "GhostTown";
        String nonExistentState  = "ZZ";
        String nonExistentZip    = "00000";
        String nonExistentSsn    = "999-99-9999";

        forgotLoginPage.fillLookupForm(nonExistentFirst, nonExistentLast, nonExistentStreet,
                nonExistentCity, nonExistentState, nonExistentZip, nonExistentSsn);
        forgotLoginPage.clickFindMyLoginInfo();

        // Assert error message indicating customer record could not be found
        String errorMsg = forgotLoginPage.getErrorMessage();
        assertTrue(errorMsg.contains("The customer could not be found") ||
                   errorMsg.contains("The customer information provided could not be found") ||
                   errorMsg.contains("could not be found") ||
                   errorMsg.contains("error"),
                "Expected customer not found error message, but got: " + errorMsg);
    }
}

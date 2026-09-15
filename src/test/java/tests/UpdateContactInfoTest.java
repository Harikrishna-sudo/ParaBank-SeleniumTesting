package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.UpdateContactInfoPage;

public class UpdateContactInfoTest extends BaseTest {

    // ============================================================
    // TEST ACCOUNT
    // ============================================================

    private String username = "john";
    private String password = "demo";

    // ============================================================
    // TEST 1
    // Verify user can successfully update their address
    // ============================================================

    @Test
    public void verifyUserCanSuccessfullyUpdateAddress() {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        // Verify login was successful
        Assert.assertTrue(
                driver.getPageSource().contains("Accounts Overview"),
                "Login failed. Accounts Overview page was not displayed."
        );

        // Step 2: Open Update Contact Info
        UpdateContactInfoPage contactPage =
                new UpdateContactInfoPage(driver);

        contactPage.clickUpdateContactInfo();

        // Step 3: Enter new address
        String street = "123 Test Street";
        String city = "Hyderabad";
        String state = "Telangana";
        String zipCode = "500001";

        contactPage.updateAddress(
                street,
                city,
                state,
                zipCode
        );

        // Step 4: Submit
        contactPage.clickUpdateProfile();

        // Step 5: Verify successful update
        Assert.assertTrue(
                driver.getPageSource().contains("Profile Updated"),
                "Profile was not updated successfully."
        );
    }


    // ============================================================
    // TEST 2
    // Verify user can successfully update phone number
    // ============================================================

    @Test
    public void verifyUserCanSuccessfullyUpdatePhoneNumber() {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        // Verify login
        Assert.assertTrue(
                driver.getPageSource().contains("Accounts Overview"),
                "Login failed. Accounts Overview page was not displayed."
        );

        // Step 2: Open Update Contact Info
        UpdateContactInfoPage contactPage =
                new UpdateContactInfoPage(driver);

        contactPage.clickUpdateContactInfo();

        // Step 3: Enter phone number
        String phoneNumber = "9876543210";

        contactPage.updatePhoneNumber(phoneNumber);

        // Step 4: Submit
        contactPage.clickUpdateProfile();

        // Step 5: Verify successful update
        Assert.assertTrue(
                driver.getPageSource().contains("Profile Updated"),
                "Phone number was not updated successfully."
        );
    }


    // ============================================================
    // TEST 3
    // Verify profile update flow remains available after navigation away and back
    // ============================================================

    @Test
    public void verifyUpdatedProfilePersistsAfterNavigation() {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        Assert.assertTrue(
                driver.getPageSource().contains("Accounts Overview"),
                "Login failed. Accounts Overview page was not displayed."
        );

        // Step 2: Open Update Contact Info
        UpdateContactInfoPage contactPage =
                new UpdateContactInfoPage(driver);

        contactPage.clickUpdateContactInfo();

        // Step 3: Enter new address and phone number
        String street = "456 Persistence Avenue";
        String city = "Chennai";
        String state = "Tamil Nadu";
        String zipCode = "600001";
        String phoneNumber = "9000000001";

        contactPage.updateAddress(street, city, state, zipCode);
        contactPage.updatePhoneNumber(phoneNumber);

        // Step 4: Submit
        contactPage.clickUpdateProfile();

        // Step 5: Verify profile update success
        Assert.assertTrue(
                driver.getPageSource().contains("Profile Updated"),
                "Profile was not updated successfully."
        );

        // Step 6: Navigate away and return
        driver.navigate().to("https://parabank.parasoft.com/parabank/overview.htm");

        Assert.assertTrue(
                driver.getPageSource().contains("Accounts Overview"),
                "Overview page was not displayed after navigation."
        );

        contactPage.clickUpdateContactInfo();

        // Step 7: Verify the contact form is available after returning
        Assert.assertTrue(
                driver.findElement(By.id("customer.address.street")).isDisplayed(),
                "Street field was not available after returning to the profile page."
        );
        Assert.assertTrue(
                driver.findElement(By.id("customer.address.city")).isDisplayed(),
                "City field was not available after returning to the profile page."
        );
        Assert.assertTrue(
                driver.findElement(By.id("customer.address.state")).isDisplayed(),
                "State field was not available after returning to the profile page."
        );
        Assert.assertTrue(
                driver.findElement(By.id("customer.address.zipCode")).isDisplayed(),
                "ZIP code field was not available after returning to the profile page."
        );
        Assert.assertTrue(
                driver.findElement(By.id("customer.phoneNumber")).isDisplayed(),
                "Phone field was not available after returning to the profile page."
        );
    }


    // ============================================================
    // TEST 4
    // Verify error when mandatory fields are cleared
    // ============================================================

    @Test
    public void verifyErrorWhenMandatoryFieldsAreCleared() {

        // Step 1: Login
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(username, password);

        // Verify login
        Assert.assertTrue(
                driver.getPageSource().contains("Accounts Overview"),
                "Login failed. Accounts Overview page was not displayed."
        );

        // Step 2: Open Update Contact Info
        UpdateContactInfoPage contactPage =
                new UpdateContactInfoPage(driver);

        contactPage.clickUpdateContactInfo();

        // Step 3: Clear mandatory address fields
        contactPage.enterStreet("");
        contactPage.enterCity("");
        contactPage.enterState("");
        contactPage.enterZipCode("");

        // Step 4: Submit the form
        contactPage.clickUpdateProfile();

        // Step 5: Verify an error is displayed
        String pageSource = driver.getPageSource();

        Assert.assertTrue(
                pageSource.contains("required")
                        || pageSource.contains("Required")
                        || pageSource.contains("error")
                        || pageSource.contains("Error"),
                "Expected validation error was not displayed."
        );
    }
}
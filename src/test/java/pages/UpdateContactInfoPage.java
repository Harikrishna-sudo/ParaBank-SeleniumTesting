package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UpdateContactInfoPage {

    private WebDriver driver;

    private WebDriverWait wait;

    // Update Contact Info link
    private By updateContactInfoLink =
            By.xpath("//a[contains(normalize-space(), 'Update Contact Info')]");

    // Address fields
    private By streetField =
            By.id("customer.address.street");

    private By cityField =
            By.id("customer.address.city");

    private By stateField =
            By.id("customer.address.state");

    private By zipCodeField =
            By.id("customer.address.zipCode");

    // Phone number
    private By phoneNumberField =
            By.id("customer.phoneNumber");

    // Update Profile button
    private By updateProfileButton =
            By.xpath("//input[@value='Update Profile']");

    // Constructor
    public UpdateContactInfoPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    // Open Update Contact Info
    public void clickUpdateContactInfo() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        updateContactInfoLink
                )
        ).click();
    }

    // Enter street
    public void enterStreet(String street) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        streetField
                )
        ).clear();

        driver.findElement(streetField)
                .sendKeys(street);
    }

    // Enter city
    public void enterCity(String city) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        cityField
                )
        ).clear();

        driver.findElement(cityField)
                .sendKeys(city);
    }

    // Enter state
    public void enterState(String state) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        stateField
                )
        ).clear();

        driver.findElement(stateField)
                .sendKeys(state);
    }

    // Enter ZIP code
    public void enterZipCode(String zipCode) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        zipCodeField
                )
        ).clear();

        driver.findElement(zipCodeField)
                .sendKeys(zipCode);
    }

    // Enter phone number
    public void enterPhoneNumber(String phoneNumber) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        phoneNumberField
                )
        ).clear();

        driver.findElement(phoneNumberField)
                .sendKeys(phoneNumber);
    }

    // Click Update Profile
    public void clickUpdateProfile() {

        wait.until(
                ExpectedConditions.elementToBeClickable(
                        updateProfileButton
                )
        ).click();
    }

    // Update complete address
    public void updateAddress(
            String street,
            String city,
            String state,
            String zipCode) {

        enterStreet(street);

        enterCity(city);

        enterState(state);

        enterZipCode(zipCode);
    }

    // Update phone number
    public void updatePhoneNumber(String phoneNumber) {

        enterPhoneNumber(phoneNumber);
    }
}
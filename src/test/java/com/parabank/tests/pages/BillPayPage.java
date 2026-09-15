package com.parabank.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BillPayPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Form field locators ---
    @FindBy(name = "payee.name")           private WebElement payeeName;
    @FindBy(name = "payee.address.street") private WebElement address;
    @FindBy(name = "payee.address.city")   private WebElement city;
    @FindBy(name = "payee.address.state")  private WebElement state;
    @FindBy(name = "payee.address.zipCode")private WebElement zipCode;
    @FindBy(name = "payee.phoneNumber")    private WebElement phone;
    @FindBy(name = "payee.accountNumber")  private WebElement accountNumber;
    @FindBy(name = "verifyAccount")        private WebElement verifyAccount;
    @FindBy(name = "amount")               private WebElement amount;
    @FindBy(css  = "input[value='Send Payment']") private WebElement sendBtn;

    public BillPayPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Navigate to bill pay page
    public void open() {
        driver.get("https://parabank.parasoft.com/parabank/billpay.htm");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("payee.name")));
    }

    // Fill the entire form with valid data
    public void fillValidForm(String amt) {
        wait.until(ExpectedConditions.visibilityOf(payeeName));
        payeeName.clear();
        payeeName.sendKeys("John Doe");

        address.clear();
        address.sendKeys("123 Main St");

        city.clear();
        city.sendKeys("Boston");

        state.clear();
        state.sendKeys("MA");

        zipCode.clear();
        zipCode.sendKeys("02101");

        phone.clear();
        phone.sendKeys("6175551234");

        accountNumber.clear();
        accountNumber.sendKeys("12345");

        verifyAccount.clear();
        verifyAccount.sendKeys("12345");

        amount.clear();
        if (amt != null && !amt.isEmpty()) {
            amount.sendKeys(amt);
        }

        // select first account in dropdown
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("fromAccountId")));
        new Select(dropdown).selectByIndex(0);
    }

    public void clickSend() {
        wait.until(ExpectedConditions.elementToBeClickable(sendBtn)).click();
    }

    // Get success message text
    public String getSuccessMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("billpayResult")))
                .getText();
    }

    // Get a specific field error message
    public String getFieldError(String fieldName) {
        try {
            WebElement el = driver.findElement(By.id(fieldName));
            if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                return el.getText().trim();
            }
        } catch (Exception ignored) {}

        try {
            WebElement el = driver.findElement(By.xpath("//span[contains(@id, '" + fieldName + "') or contains(@class, 'error')]"));
            if (el.isDisplayed()) {
                return el.getText().trim();
            }
        } catch (Exception ignored) {}

        return "";
    }

    // Check if account mismatch error is shown
    public boolean isAccountMismatchErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//span[text()='The account numbers do not match.']")
            )).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

package com.parabank.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForgotLoginInfoPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "address.street")
    private WebElement streetInput;

    @FindBy(id = "address.city")
    private WebElement cityInput;

    @FindBy(id = "address.state")
    private WebElement stateInput;

    @FindBy(id = "address.zipCode")
    private WebElement zipCodeInput;

    @FindBy(id = "ssn")
    private WebElement ssnInput;

    @FindBy(css = "input[value='Find My Login Info']")
    private WebElement findMyLoginInfoBtn;

    public ForgotLoginInfoPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void open(String baseUrl) {
        driver.get(baseUrl + "/lookup.htm");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstName")));
    }

    public void fillLookupForm(String firstName, String lastName, String street,
                               String city, String state, String zipCode, String ssn) {
        WebElement first = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("firstName")));
        first.clear();
        first.sendKeys(firstName);

        driver.findElement(By.id("lastName")).clear();
        driver.findElement(By.id("lastName")).sendKeys(lastName);

        driver.findElement(By.id("address.street")).clear();
        driver.findElement(By.id("address.street")).sendKeys(street);

        driver.findElement(By.id("address.city")).clear();
        driver.findElement(By.id("address.city")).sendKeys(city);

        driver.findElement(By.id("address.state")).clear();
        driver.findElement(By.id("address.state")).sendKeys(state);

        driver.findElement(By.id("address.zipCode")).clear();
        driver.findElement(By.id("address.zipCode")).sendKeys(zipCode);

        driver.findElement(By.id("ssn")).clear();
        driver.findElement(By.id("ssn")).sendKeys(ssn);
    }

    public void clickFindMyLoginInfo() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Find My Login Info']")));
        btn.click();
    }

    public String getResultText() {
        // Wait until rightPanel contains text different from the empty lookup form
        wait.until(driver -> {
            try {
                WebElement panel = driver.findElement(By.id("rightPanel"));
                String text = panel.getText();
                return text.contains("Your login information was located") ||
                       text.contains("could not be found") ||
                       text.contains("Error") ||
                       driver.findElements(By.id("firstName")).isEmpty();
            } catch (Exception e) {
                return false;
            }
        });

        WebElement resultContainer = driver.findElement(By.id("rightPanel"));
        return resultContainer.getText();
    }

    public String getErrorMessage() {
        try {
            WebElement errorEl = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='rightPanel']//p[@class='error' or contains(@class, 'error')]"))
            );
            return errorEl.getText().trim();
        } catch (Exception e) {
            return getResultText();
        }
    }
}

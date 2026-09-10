package com.parabank.tests.openNewAccount;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenSavingsAccountTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BASE_URL =
            "https://parabank-17m8.onrender.com/parabank/index.htm";

    private static final String USERNAME = "Patrick Jane";
    private static final String PASSWORD = "Jane@123";

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();

        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );

        driver.manage().window().maximize();

        // Open ParaBank
        driver.get(BASE_URL);

        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("username")
        )).sendKeys(USERNAME);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.name("password")
        )).sendKeys(PASSWORD);

        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[type='submit']")
        )).click();

        // Wait until login has completed
        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Open New Account")
        ));
    }

    @Test
    @DisplayName("Successfully open a new CHECKING account funded from an existing account")
    void openCheckingAccount() {

        // Navigate to Open New Account
        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Open New Account")
        )).click();

        // Wait for account type dropdown
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("type")
        ));

        Select accountType = new Select(
                driver.findElement(By.id("type"))
        );

        // Select SAVINGS
        accountType.selectByValue("1");

        // Wait for the funding account dropdown
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("fromAccountId")
        ));

        // Wait until the required account option is actually present
        wait.until(driver -> {
            Select select = new Select(
                    driver.findElement(By.id("fromAccountId"))
            );

            return select.getOptions()
                    .stream()
                    .anyMatch(option ->
                            option.getAttribute("value").equals("13566")
                    );
        });

        // Select existing account
        Select fromAccount = new Select(
                driver.findElement(By.id("fromAccountId"))
        );

        int numberOfAccounts = fromAccount.getOptions().size();

        int randomIndex = new Random().nextInt(numberOfAccounts);

        fromAccount.selectByIndex(randomIndex);

        // Wait until the Open New Account button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[value='Open New Account']")
        )).click();

        // Wait for account creation result
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("openAccountResult")
        ));

        // Wait until the newly generated account number is populated
        wait.until(driver -> {
            String accountNumber = driver.findElement(
                    By.id("newAccountId")
            ).getText().trim();

            return !accountNumber.isEmpty();
        });

        // Get the newly created account number
        String newAccountNumber = driver.findElement(
                By.id("newAccountId")
        ).getText().trim();

        // Verify account number was generated
        assertFalse(
                newAccountNumber.isEmpty(),
                "New account number should be generated."
        );

        // Verify successful account creation
        String pageText = driver.findElement(
                By.id("openAccountResult")
        ).getText();

        assertTrue(
                pageText.contains("Account Opened"),
                "The CHECKING account was not opened successfully."
        );
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
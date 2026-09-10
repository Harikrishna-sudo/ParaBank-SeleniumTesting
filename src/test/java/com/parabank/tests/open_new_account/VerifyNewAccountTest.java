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
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifyNewAccountTest {

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
    @DisplayName("Verify the new account details after creation")
    void verifyNewAccount() {

        Random random = new Random();

        // Navigate to Open New Account
        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Open New Account")
        )).click();

        // Wait for account type dropdown
        Select accountType = new Select(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("type")
                ))
        );

        // Randomly select CHECKING or SAVING
        int accountTypeIndex = random.nextInt(
                accountType.getOptions().size()
        );

        accountType.selectByIndex(accountTypeIndex);

        // Store the selected account type for later verification
        String expectedAccountType = accountType
                .getFirstSelectedOption()
                .getText()
                .trim();

        // Wait until the funding account dropdown is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("fromAccountId")
        ));

        // Wait until at least one existing account is available
        wait.until(driver -> {
            Select select = new Select(
                    driver.findElement(By.id("fromAccountId"))
            );

            return !select.getOptions().isEmpty();
        });

        // Get the funding account dropdown after its options are loaded
        Select fromAccount = new Select(
                driver.findElement(By.id("fromAccountId"))
        );

        // Randomly select an existing account
        int randomAccountIndex = random.nextInt(
                fromAccount.getOptions().size()
        );

        fromAccount.selectByIndex(randomAccountIndex);

        // Submit the request
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[value='Open New Account']")
        )).click();

        // Wait for account creation result
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("openAccountResult")
        ));

        // Verify successful account creation
        String pageText = driver.findElement(
                By.id("openAccountResult")
        ).getText();

        assertTrue(
                pageText.contains("Account Opened"),
                "The account was not opened successfully."
        );

        // Wait until the dynamically generated account number is available
        String newAccountNumber = Objects.requireNonNull(
                wait.until(driver -> {

                    String accountNumber = driver.findElement(
                            By.id("newAccountId")
                    ).getText().trim();

                    return accountNumber.isEmpty()
                            ? null
                            : accountNumber;
                }),
                "New account number should be generated."
        );

        // Click the newly created account
        wait.until(ExpectedConditions.elementToBeClickable(
                By.id("newAccountId")
        )).click();

        // Wait until the account details page contains the generated account number
        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("accountId"),
                        newAccountNumber
                )
        );

        // Get the account number from the account details page
        String actualAccountNumber = driver.findElement(
                By.id("accountId")
        ).getText().trim();

        // Wait until the account type appears
        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.id("accountType"),
                        expectedAccountType
                )
        );

        // Get the account type from the account details page
        String actualAccountType = driver.findElement(
                By.id("accountType")
        ).getText().trim();

        // Verify account number
        assertEquals(
                newAccountNumber,
                actualAccountNumber,
                "Account number mismatch. Expected: "
                        + newAccountNumber
                        + ", Actual: "
                        + actualAccountNumber
        );

        // Verify account type
        assertEquals(
                expectedAccountType,
                actualAccountType,
                "Account type mismatch. Expected: "
                        + expectedAccountType
                        + ", Actual: "
                        + actualAccountType
        );

        System.out.println("Expected Account Number: " + newAccountNumber);
        System.out.println("Actual Account Number: " + actualAccountNumber);
        System.out.println("Expected Account Type: " + expectedAccountType);
        System.out.println("Actual Account Type: " + actualAccountType);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenSavingsAccountTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final String BASE_URL =
            "https://parabank-17m8.onrender.com/parabank/index.htm";

    private static final String USERNAME = "john";
    private static final String PASSWORD = "demo";

    @BeforeEach
    void setUp() {

        // Start Chrome browser
        driver = new ChromeDriver();

        // Explicit wait
        wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(30)
        );

        // Maximize browser
        driver.manage().window().maximize();

        // Open ParaBank
        driver.get(BASE_URL);

        // -----------------------------
        // LOGIN
        // -----------------------------

        WebElement usernameField =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.name("username")
                ));

        usernameField.clear();
        usernameField.sendKeys(USERNAME);

        WebElement passwordField =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.name("password")
                ));

        passwordField.clear();
        passwordField.sendKeys(PASSWORD);

        // Click Login
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[type='submit']")
        )).click();

        // Wait until login completes
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.linkText("Open New Account")
        ));

        System.out.println("Login successful.");
    }

    @Test
    @DisplayName(
            "TS018 - Successfully open a new SAVINGS account funded from an existing account"
    )
    void openSavingsAccount() {

        // -----------------------------
        // STEP 1: OPEN NEW ACCOUNT PAGE
        // -----------------------------

        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Open New Account")
        )).click();

        // -----------------------------
        // STEP 2: SELECT SAVINGS
        // -----------------------------

        WebElement accountTypeElement =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("type")
                ));

        Select accountType =
                new Select(accountTypeElement);

        // ParaBank:
        // 0 = CHECKING
        // 1 = SAVINGS
        accountType.selectByValue("1");

        System.out.println("Account type selected: SAVINGS");

        // -----------------------------
        // STEP 3: WAIT FOR FUNDING ACCOUNTS
        // -----------------------------

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("fromAccountId")
        ));

        /*
         * IMPORTANT:
         *
         * Do NOT wait for a specific account number
         * such as 13566.
         *
         * ParaBank generates different account numbers.
         *
         * Instead, wait until at least one account
         * exists in the dropdown.
         */
        wait.until(webDriver -> {

            Select fundingDropdown = new Select(
                    webDriver.findElement(By.id("fromAccountId"))
            );

            List<WebElement> accounts =
                    fundingDropdown.getOptions();

            return !accounts.isEmpty();
        });

        // -----------------------------
        // STEP 4: SELECT FIRST ACCOUNT
        // -----------------------------

        WebElement fromAccountElement =
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.id("fromAccountId")
                ));

        Select fromAccount =
                new Select(fromAccountElement);

        List<WebElement> availableAccounts =
                fromAccount.getOptions();

        // Verify that at least one funding account exists
        assertFalse(
                availableAccounts.isEmpty(),
                "No funding accounts are available."
        );

        /*
         * Select the first/top account.
         *
         * No account number is hard-coded.
         */
        fromAccount.selectByIndex(0);

        // Get the selected funding account number
        String selectedAccount =
                fromAccount
                        .getFirstSelectedOption()
                        .getText()
                        .trim();

        System.out.println(
                "Funding account selected: " + selectedAccount
        );

        // -----------------------------
        // STEP 5: OPEN SAVINGS ACCOUNT
        // -----------------------------

        WebElement openAccountButton =
                wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(
                                "input[value='Open New Account']"
                        )
                ));

        openAccountButton.click();

        // -----------------------------
        // STEP 6: WAIT FOR RESULT
        // -----------------------------

        WebElement result =
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.id("openAccountResult")
                ));

        // -----------------------------
        // STEP 7: WAIT FOR NEW ACCOUNT NUMBER
        // -----------------------------

        wait.until(webDriver -> {

            WebElement newAccountElement =
                    webDriver.findElement(
                            By.id("newAccountId")
                    );

            String accountNumber =
                    newAccountElement.getText().trim();

            return !accountNumber.isEmpty();
        });

        // -----------------------------
        // STEP 8: GET NEW ACCOUNT NUMBER
        // -----------------------------

        String newAccountNumber =
                driver.findElement(
                        By.id("newAccountId")
                ).getText().trim();

        System.out.println(
                "New SAVINGS account number: "
                        + newAccountNumber
        );

        // -----------------------------
        // STEP 9: VERIFY ACCOUNT NUMBER
        // -----------------------------

        assertFalse(
                newAccountNumber.isEmpty(),
                "New SAVINGS account number should be generated."
        );

        // -----------------------------
        // STEP 10: VERIFY SUCCESS MESSAGE
        // -----------------------------

        String resultText =
                result.getText();

        System.out.println(
                "Account creation result: "
                        + resultText
        );

        assertTrue(
                resultText.contains("Account Opened"),
                "The SAVINGS account was not opened successfully."
        );

        System.out.println(
                "TEST PASSED - SAVINGS account "
                        + newAccountNumber
                        + " created using funding account "
                        + selectedAccount
        );
    }

    @AfterEach
    void tearDown() {

        // Close browser after test
        if (driver != null) {
            driver.quit();
        }
    }
}
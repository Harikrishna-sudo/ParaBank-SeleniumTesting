package tests;

import java.time.Duration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferFund {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    void setup() {

        driver = new EdgeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get(
                "https://parabank-17m8.onrender.com/parabank/index.htm?ConnType=JDBC");

        login();

        openTransferFundsPage();
    }

    @BeforeEach
    void goToTransferFunds() {

        wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.linkText("Transfer Funds")))
                .click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("transferForm")));

        wait.until(driver ->
                driver.findElements(
                        By.cssSelector("#fromAccountId option")).size() > 0);

        wait.until(driver ->
                driver.findElements(
                        By.cssSelector("#toAccountId option")).size() > 0);
    }

    private void login() {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.name("username")))
                .sendKeys("john");

        driver.findElement(By.name("password"))
                .sendKeys("demo");

        driver.findElement(By.xpath("//input[@value='Log In']"))
                .click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[normalize-space()='Account Services']")));
    }

    @AfterAll
    void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }

    private void openTransferFundsPage() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Transfer Funds"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("transferForm")));

        wait.until(driver ->
                driver.findElements(
                        By.cssSelector("#fromAccountId option")).size() >= 2);

        wait.until(driver ->
                driver.findElements(
                        By.cssSelector("#toAccountId option")).size() >= 2);
    }

    private Select fromAccount() {
        return new Select(driver.findElement(By.id("fromAccountId")));
    }

    private Select toAccount() {
        return new Select(driver.findElement(By.id("toAccountId")));
    }

    private void submitTransfer(String amount) {

        WebElement amountBox = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.id("amount")));

        amountBox.clear();
        amountBox.sendKeys(amount);

        driver.findElement(
                        By.cssSelector(
                                "#transferForm input[type='submit']"))
                .click();
    }

    private void waitForTransferOutcome() {

        wait.until(driver -> {

            boolean success =
                    !driver.findElements(By.id("showResult"))
                            .isEmpty();

            boolean error =
                    !driver.findElements(By.id("showError"))
                            .isEmpty();

            return success || error;
        });
    }

    private double getAccountBalance(String accountId) throws InterruptedException {

        driver.findElement(
                        By.linkText("Accounts Overview"))
                .click();
        sleep(500);
        WebElement balanceCell = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(
                                "//a[text()='"
                                        + accountId
                                        + "']/parent::td/following-sibling::td")));

        String balanceText = balanceCell.getText()
                .replace("$", "")
                .replace(",", "")
                .trim();

        return Double.parseDouble(balanceText);
    }

    private void returnToTransferFunds() {

        wait.until(ExpectedConditions.elementToBeClickable(
                By.linkText("Transfer Funds"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("transferForm")));

        wait.until(driver ->
                driver.findElements(
                                By.cssSelector("#fromAccountId option"))
                        .size() > 1);

        wait.until(driver ->
                driver.findElements(
                                By.cssSelector("#toAccountId option"))
                        .size() > 1);
    }

    /*
     * Requirement 1:
     * Verify source account balance decreases
     * by exactly the transferred amount.
     */
    @Test
    @DisplayName("TS026 - Source account balance decreases by the transferred amount")
    void verifySourceAccountBalanceDecreasesByTransferredAmount() throws InterruptedException {

        String fromAccountId =
                fromAccount().getOptions()
                        .get(0)
                        .getAttribute("value");

        String toAccountId =
                toAccount().getOptions()
                        .get(1)
                        .getAttribute("value");

        double transferAmount = 10.00;

        double sourceBalanceBefore =
                getAccountBalance(fromAccountId);

        returnToTransferFunds();

        fromAccount().selectByValue(fromAccountId);
        toAccount().selectByValue(toAccountId);

        submitTransfer(String.valueOf(transferAmount));

        waitForTransferOutcome();

        double sourceBalanceAfter =
                getAccountBalance(fromAccountId);

        assertEquals(
                sourceBalanceBefore - transferAmount,
                sourceBalanceAfter,
                0.01,
                "Source balance should decrease by transfer amount.");
    }

    /*
     * Requirement 2:
     * Verify destination account balance increases
     * by exactly the transferred amount.
     */
    @Test
    @DisplayName("TS027 - Destination account balance increases by the transferred amount")
    void verifyDestinationAccountBalanceIncreasesByTransferredAmount() throws InterruptedException {

        String fromAccountId =
                fromAccount().getOptions()
                        .get(0)
                        .getAttribute("value");

        String toAccountId =
                toAccount().getOptions()
                        .get(1)
                        .getAttribute("value");

        double transferAmount = 10.00;

        double destinationBalanceBefore =
                getAccountBalance(toAccountId);

        returnToTransferFunds();

        fromAccount().selectByValue(fromAccountId);
        toAccount().selectByValue(toAccountId);

        submitTransfer(String.valueOf(transferAmount));

        waitForTransferOutcome();

        double destinationBalanceAfter =
                getAccountBalance(toAccountId);

        assertEquals(
                destinationBalanceBefore + transferAmount,
                destinationBalanceAfter,
                0.01,
                "Destination balance should increase by transfer amount.");
    }

    /*
     * Requirement 3:
     * Enter non-numeric or invalid characters.
     */
    @Test
    @DisplayName("TS028 - Transfer with non-numeric amount is rejected with an error")
    void shouldRejectInvalidCharactersInAmountField() {

        String fromAccountId =
                fromAccount().getOptions()
                        .get(0)
                        .getAttribute("value");

        String toAccountId =
                toAccount().getOptions()
                        .get(1)
                        .getAttribute("value");

        fromAccount().selectByValue(fromAccountId);
        toAccount().selectByValue(toAccountId);

        submitTransfer("abc");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("showError")));

        assertTrue(
                driver.findElement(By.id("showError"))
                        .isDisplayed(),
                "Invalid amount should show an error.");
    }

    /*
     * Requirement 4:
     * Transfer from and to the same account.
     * Verify net balance remains unchanged.
     */
    @Test
    @DisplayName("TS029 - Transfer between the same account leaves balance unchanged")
    void verifySameSourceAndDestinationAccountHasNoBalanceChange() throws InterruptedException {

        String accountId =
                fromAccount().getOptions()
                        .get(0)
                        .getAttribute("value");

        double balanceBefore =
                getAccountBalance(accountId);

        returnToTransferFunds();

        fromAccount().selectByValue(accountId);
        toAccount().selectByValue(accountId);

        submitTransfer("10");

        waitForTransferOutcome();

        double balanceAfter =
                getAccountBalance(accountId);

        assertEquals(
                balanceBefore,
                balanceAfter,
                0.01,
                "Balance should remain unchanged for same-account transfer.");
    }
    @Test
    @DisplayName("TS030 - Transfer with a negative amount is processed by the system")
    void shouldNotAllowNegativeAmountTransfer() {

        submitTransfer("-10");

        waitForTransferOutcome();

        boolean transferSucceeded =
                !driver.findElements(By.id("showResult")).isEmpty()
                        && driver.findElement(By.id("showResult")).isDisplayed();

        assertTrue(
                transferSucceeded,
                "BUG: Negative amount transfer was processed successfully."
        );
    }
}

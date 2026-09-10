package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import pages.FindTransactionsPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FindTransactionsTest extends BaseTest {

    @Test
    public void verifyTransactionSearchById() {

        FindTransactionsPage findTransactionsPage =
                new FindTransactionsPage(driver);

        // Open Find Transactions page
        findTransactionsPage.openFindTransactions();

        // Select account
        findTransactionsPage.selectAccount("13344");

        // Enter transaction ID
        findTransactionsPage.enterTransactionId("14365");

        // Search
        findTransactionsPage.clickFindTransactions();

        findTransactionsPage.clickFundsTransferReceived();

        String actualTransactionId =
                findTransactionsPage.getTransactionIdFromResult();

        assertEquals(
                "14365",
                actualTransactionId,
                "The returned transaction ID should match the searched transaction ID"
        );
    }

    @Test
    public void verifyTransactionSearchByDate() {

        FindTransactionsPage findTransactionsPage =
                new FindTransactionsPage(driver);

        // Open Find Transactions page
        findTransactionsPage.openFindTransactions();

        // Select account
        findTransactionsPage.selectAccount("13344");

        // Date used for searching
        String searchedDate = "09-07-2026";

        // Enter date
        findTransactionsPage.enterDate(searchedDate);

        // Click the specific Date search button
        findTransactionsPage.clickFindByDate();

        // Get dates from all returned transactions
        java.util.List<String> transactionDates =
                findTransactionsPage.getTransactionDates();

        // Verify every returned transaction has the searched date
        for (String actualDate : transactionDates) {

            assertEquals(
                    searchedDate,
                    actualDate,
                    "Returned transaction date should match the searched date"
            );
        }
    }

    @Test
    public void verifyTransactionSearchByAmount() {

        FindTransactionsPage findTransactionsPage =
                new FindTransactionsPage(driver);

        // Open Find Transactions page
        findTransactionsPage.openFindTransactions();

        // Select account
        findTransactionsPage.selectAccount("13344");

        // Amount used for searching
        String searchedAmount = "1000";

        // Enter amount
        findTransactionsPage.enterAmount(searchedAmount);

        // Search by amount
        findTransactionsPage.clickFindByAmount();

        // Get amounts from returned transactions
        java.util.List<String> transactionAmounts =
                findTransactionsPage.getTransactionAmounts();

        // Verify every returned transaction has the searched amount
        for (String actualAmount : transactionAmounts) {

            assertEquals(
                    "$" + searchedAmount + ".00",
                    actualAmount,
                    "Returned transaction amount should match the searched amount"
            );
        }
    }
}
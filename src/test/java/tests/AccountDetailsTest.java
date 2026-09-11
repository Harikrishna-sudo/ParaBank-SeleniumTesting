package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.RequestLoanPage;
import pages.AccountsOverviewPage;
import pages.AccountDetailsPage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDetailsTest extends BaseTest {

    @Test
    public void verifyAccountDetails() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");

        RequestLoanPage loanPage = new RequestLoanPage(driver);
        loanPage.openRequestLoan();

        loanPage.enterLoanAmount("100");
        loanPage.enterDownPayment("50");
        loanPage.selectFirstAvailableAccount();

        loanPage.clickApplyNow();

        assertEquals("Approved", loanPage.getLoanStatus(), "Loan request was not approved");

        String newAccountNumber = loanPage.getNewAccountNumber();

        System.out.println("New Account Number: " + newAccountNumber);

        AccountsOverviewPage accountsPage = new AccountsOverviewPage(driver);

        accountsPage.openAccountsOverview();

        assertTrue(
                accountsPage.isAccountDisplayed(newAccountNumber), "New account was not displayed in Accounts Overview");

        accountsPage.openAccount(newAccountNumber);

        AccountDetailsPage accountDetailsPage = new AccountDetailsPage(driver);

        String displayedAccountNumber = accountDetailsPage.getAccountNumber();

        String accountType = accountDetailsPage.getAccountType();

        String balance = accountDetailsPage.getBalance();

        String availableBalance = accountDetailsPage.getAvailableBalance();

        System.out.println("Account Number: " + displayedAccountNumber);

        System.out.println("Account Type: " + accountType);

        System.out.println("Balance: " + balance);

        System.out.println("Available Balance: " + availableBalance);

        assertEquals(newAccountNumber, displayedAccountNumber, "Account number does not match the newly created loan account");

        assertFalse(accountType.isEmpty(), "Account type is not displayed");

        assertFalse(balance.isEmpty(), "Balance is not displayed");

        assertFalse(availableBalance.isEmpty(), "Available balance is not displayed");
    }
}
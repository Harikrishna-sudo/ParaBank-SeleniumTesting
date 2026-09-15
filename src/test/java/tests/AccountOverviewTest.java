package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.RequestLoanPage;
import pages.AccountsOverviewPage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountOverviewTest extends BaseTest {

    @Test
    public void verifyApprovedLoanCreatesNewAccount() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");

        RequestLoanPage loanPage = new RequestLoanPage(driver);

        loanPage.openRequestLoan();

        loanPage.enterLoanAmount("100");
        loanPage.enterDownPayment("50");

        loanPage.selectFirstAvailableAccount();

        loanPage.clickApplyNow();

        assertTrue(loanPage.isLoanRequestProcessed(), "Loan request was not processed");

        assertTrue(loanPage.isLoanApproved(), "Loan request was not approved");

        String newAccountNumber = loanPage.getNewAccountNumber();

        System.out.println("New Account Number: " + newAccountNumber);

        AccountsOverviewPage accountsPage = new AccountsOverviewPage(driver);

        accountsPage.openAccountsOverview();

        assertTrue(accountsPage.isAccountDisplayed(newAccountNumber), "New loan account was not displayed in Accounts Overview");
    }
}
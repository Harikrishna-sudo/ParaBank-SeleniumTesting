package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.RequestLoanPage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestLoanResultTest extends BaseTest {

    @Test
    @DisplayName("TS024 - Loan result status displays Denied for an unqualified application")
    public void verifyLoanResultStatus() {

        driver.get(baseUrl+"/index.htm");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");

        RequestLoanPage loanPage = new RequestLoanPage(driver);
        loanPage.openRequestLoan();

        loanPage.enterLoanAmount("50000");
        loanPage.enterDownPayment("1");

        loanPage.selectFirstAvailableAccount();

        loanPage.clickApplyNow();

        String status = loanPage.getLoanStatus();

        System.out.println("Loan Status: " + status);

        assertEquals(
                "Denied",
                status,
                "Incorrect loan status displayed"
        );
    }
}
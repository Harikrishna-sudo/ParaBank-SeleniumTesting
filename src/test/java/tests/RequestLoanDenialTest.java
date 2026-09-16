package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.RequestLoanPage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestLoanDenialTest extends BaseTest {

    @Test
    @DisplayName("TS023 - Loan request with insufficient down payment is denied")
    public void verifyLoanRequestDenial() {

        driver.get(baseUrl+"/index.htm");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");

        RequestLoanPage loanPage = new RequestLoanPage(driver);
        loanPage.openRequestLoan();

        loanPage.enterLoanAmount("100");
        loanPage.enterDownPayment("1");

        loanPage.selectFirstAvailableAccount();

        loanPage.clickApplyNow();

        String result = loanPage.getResultText();

        System.out.println();
        System.out.println("Loan Result:");
        System.out.println(result);

        assertTrue(
                loanPage.isLoanRequestProcessed(),
                "Loan Request Processed message was not displayed"
        );

        assertTrue(
                loanPage.isLoanDenied(),
                "Loan request was not denied"
        );
    }
}

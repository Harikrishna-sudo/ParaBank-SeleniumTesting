package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.RequestLoanPage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestLoanDenialTest extends BaseTest {

    @Test
    public void verifyLoanRequestDenial() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");

        RequestLoanPage loanPage = new RequestLoanPage(driver);
        loanPage.openRequestLoan();

        loanPage.enterLoanAmount("50000");
        loanPage.enterDownPayment("0");

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

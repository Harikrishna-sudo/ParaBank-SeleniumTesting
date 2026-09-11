package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.RequestLoanPage;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestLoanValidationTest extends BaseTest {

    @ParameterizedTest
    @CsvSource({
            ",50",
            "0,50",
            "100,",
            "100,0",
            "-100, 50",
            "100, -50"
    })
    public void verifyInvalidLoanRequest(String loanAmount, String downPayment) {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("john", "demo");

        RequestLoanPage loanPage = new RequestLoanPage(driver);
        loanPage.openRequestLoan();

        if (loanAmount != null) {
            loanPage.enterLoanAmount(loanAmount);
        }

        if (downPayment != null) {
            loanPage.enterDownPayment(downPayment);
        }

        loanPage.selectFirstAvailableAccount();

        loanPage.clickApplyNow();

        String result = loanPage.getResultText();

        System.out.println();
        System.out.println("Loan Amount: " + loanAmount);
        System.out.println("Down Payment: " + downPayment);
        System.out.println("Result:");
        System.out.println(result);

        if (loanAmount == null ||
                "0".equals(loanAmount) ||
                downPayment == null) {

            assertTrue(
                    loanPage.isInternalErrorDisplayed(), "Expected validation/error behavior was not displayed"
            );

        } else {

            assertTrue(
                    loanPage.isLoanDenied(), "Invalid loan request was not denied"
            );
        }
    }
}
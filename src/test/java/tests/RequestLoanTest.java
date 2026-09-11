package tests;

import base.BaseTest;
import pages.LoginPage;
import pages.RequestLoanPage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequestLoanTest extends BaseTest {

    @Test
    public void verifyLoanRequestApproval() {

        // Step 1: Login

        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("john", "demo");


        // Step 2: Open Request Loan

        RequestLoanPage loanPage = new RequestLoanPage(driver);

        loanPage.openRequestLoan();


        // Step 3: Enter Loan Amount

        loanPage.enterLoanAmount("100");


        // Step 4: Enter Down Payment

        loanPage.enterDownPayment("50");

        // Step 5: select account

        loanPage.selectFirstAvailableAccount();

        // Step 6: Verify entered data

        System.out.println("Loan Amount entered: " + loanPage.getLoanAmountValue());

        System.out.println("Down Payment entered: " + loanPage.getDownPaymentValue());

        System.out.println("Selected Account: " + loanPage.getSelectedAccount());


        // Verify Loan Amount
        assertEquals("100", loanPage.getLoanAmountValue(), "Loan Amount was not entered correctly");


        // Verify Down Payment
        assertEquals("50", loanPage.getDownPaymentValue(), "Down Payment was not entered correctly");


        // Verify an actual account was selected
        assertTrue(!loanPage.getSelectedAccount().isEmpty(), "No account was selected");


        // Step 7: Submit Loan Request

        loanPage.clickApplyNow();


        // Step 8: Get Result


        String result = loanPage.getResultText();

        System.out.println();
        System.out.println("Loan Result:");
        System.out.println(result);



        // Step 9: Verify Loan Processed

        assertTrue(loanPage.isLoanRequestProcessed(), "Loan Request Processed message was not displayed");


        // Step 10: Verify Approval

        assertTrue(loanPage.isLoanApproved(), "Loan request was not approved");
    }
}
package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RequestLoanPage {

    private WebDriver driver;

    // Locators
    private By requestLoanLink = By.linkText("Request Loan");

    private By loanAmount = By.id("amount");

    private By downPayment = By.id("downPayment");

    private By fromAccount = By.id("fromAccountId");

    private By applyNowButton = By.xpath("//input[@value='Apply Now']");

    private By resultSection = By.id("rightPanel");


    // Constructor
    public RequestLoanPage(WebDriver driver) {
        this.driver = driver;
    }


    // Open Request Loan page
    public void openRequestLoan() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(
                ExpectedConditions.elementToBeClickable(requestLoanLink)).click();
    }


    // Enter Loan Amount
    public void enterLoanAmount(String amount) {

        driver.findElement(loanAmount).clear();

        driver.findElement(loanAmount).sendKeys(amount);
    }


    // Enter Down Payment
    public void enterDownPayment(String amount) {

        driver.findElement(downPayment).clear();

        driver.findElement(downPayment).sendKeys(amount);
    }



    // Select first actual account
    // Skips the empty/default option
    public void selectFirstAvailableAccount() {

        Select accountDropdown =
                new Select(driver.findElement(fromAccount));

        for (int i = 0; i < accountDropdown.getOptions().size(); i++) {

            String account = accountDropdown.getOptions().get(i).getText().trim();

            if (!account.isEmpty()) {

                accountDropdown.selectByIndex(i);
                return;
            }
        }

        throw new RuntimeException(
                "No available account was found"
        );
    }


    // Click Apply Now
    public void clickApplyNow() {
        driver.findElement(applyNowButton).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Get result text
    public String getResultText() {

        return driver.findElement(resultSection).getText();
    }


    public String getLoanStatus() {
        String result = getResultText();

        if (result.contains("Status: Approved")) {
            return "Approved";
        }

        if (result.contains("Status: Denied")) {
            return "Denied";
        }

        return "Unknown";
    }



    // Check Loan Request Processed
    public boolean isLoanRequestProcessed() {

        return getResultText().contains("Loan Request Processed");
    }


    // Check Loan Approved
    public boolean isLoanApproved() {

        return getResultText().contains("Status: Approved");
    }

    //Check loan denial
    public boolean isLoanDenied() {
        return getResultText().contains("Status: Denied");
    }



    // get entered Loan Amount
    public String getLoanAmountValue() {

        return driver.findElement(loanAmount).getAttribute("value");
    }


    // get entered Down Payment
    public String getDownPaymentValue() {

        return driver.findElement(downPayment).getAttribute("value");
    }


    // get selected Account
    public String getSelectedAccount() {

        Select accountDropdown = new Select(driver.findElement(fromAccount));

        return accountDropdown.getFirstSelectedOption().getText().trim();
    }

    public String getNewAccountNumber() {
        String result = getResultText();

        String text = "Your new account number:";

        int start = result.indexOf(text);

        if (start == -1) {
            throw new RuntimeException("New account number was not displayed");
        }

        return result.substring(start + text.length()).trim();
    }

    public boolean isInternalErrorDisplayed() {
        return getResultText().contains("An internal error has occurred");
    }
}
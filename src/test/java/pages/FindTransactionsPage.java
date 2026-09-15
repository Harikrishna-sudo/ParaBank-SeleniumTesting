package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.WebElement;

public class FindTransactionsPage {

    WebDriver driver;

    private By findTransactionsLink =
            By.linkText("Find Transactions");

    private By accountDropdown =
            By.id("accountId");

    private By transactionIdField =
            By.id("transactionId");

    private By dateField =
            By.id("transactionDate");

    private By findByDateButton =
            By.id("findByDate");

    private By transactionRows =
            By.cssSelector("#transactionBody tr");

    private By amountField =
            By.id("amount");

    private By findByAmountButton =
            By.id("findByAmount");

    public FindTransactionsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openFindTransactions() {

        driver.findElement(findTransactionsLink).click();
    }

    public void selectAccount(String accountNumber) {

        driver.findElement(accountDropdown)
                .sendKeys(accountNumber);
    }

    public void enterTransactionId(String transactionId) {

        driver.findElement(transactionIdField)
                .sendKeys(transactionId);
    }


    public void clickFindTransactions() {

        driver.findElement(
                By.xpath("//button[contains(text(),'Find Transactions')]")
        ).click();
    }

    public void clickFundsTransferReceived() {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

        By fundsTransferReceived =
                By.xpath("//a[contains(normalize-space(.), 'Funds Transfer Received')]");

        wait.until(
                ExpectedConditions.elementToBeClickable(fundsTransferReceived)
        ).click();
    }
    private By transactionIdResult =
            By.xpath("//td[b[contains(text(),'Transaction ID')]]/following-sibling::td");

    public String getTransactionIdFromResult() {
        return driver.findElement(transactionIdResult).getText();
    }

    public void enterDate(String date) {
        driver.findElement(dateField)
                .sendKeys(date);
    }

    public void clickFindByDate() {
        driver.findElement(findByDateButton)
                .click();
    }

    public List<String> getTransactionDates() {

        List<String> dates = new java.util.ArrayList<>();

        for (org.openqa.selenium.WebElement row :
                driver.findElements(transactionRows)) {

            dates.add(row.findElement(By.tagName("td")).getText());
        }

        return dates;
    }

    public void enterAmount(String amount) {
        driver.findElement(amountField)
                .sendKeys(amount);
    }

    public void clickFindByAmount() {
        driver.findElement(findByAmountButton)
                .click();
    }

    public List<String> getTransactionAmounts() {

        List<String> amounts = new java.util.ArrayList<>();

        for (WebElement row :
                driver.findElements(transactionRows)) {

            amounts.add(
                    row.findElements(By.tagName("td"))
                            .get(2)
                            .getText()
            );
        }

        return amounts;
    }

}
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountDetailsPage {

    private WebDriver driver;

    private By accountNumber = By.id("accountId");
    private By accountType = By.id("accountType");
    private By balance = By.id("balance");
    private By availableBalance = By.id("availableBalance");

    public AccountDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getAccountNumber() {
        return driver.findElement(accountNumber).getText().trim();
    }

    public String getAccountType() {
        return driver.findElement(accountType).getText().trim();
    }

    public String getBalance() {
        return driver.findElement(balance).getText().trim();
    }

    public String getAvailableBalance() {
        return driver.findElement(availableBalance).getText().trim();
    }
}

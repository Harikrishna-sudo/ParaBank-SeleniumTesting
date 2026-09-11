package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountsOverviewPage {

    private WebDriver driver;

    private By accountsOverviewLink = By.linkText("Accounts Overview");

    private By accountTable = By.id("accountTable");

    public AccountsOverviewPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openAccountsOverview() {
        driver.findElement(accountsOverviewLink).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isAccountDisplayed(String accountNumber) {
        return driver.findElement(accountTable).getText().contains(accountNumber);
    }

    public void openAccount(String accountNumber) {
        driver.findElement(By.linkText(accountNumber)).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

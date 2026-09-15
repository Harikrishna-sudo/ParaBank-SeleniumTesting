package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private WebDriver driver;

    // Locators
    private By username = By.name("username");

    private By password = By.name("password");

    private By loginButton = By.cssSelector("input[type='submit']");

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Enter username
    public void enterUsername(String user) {
        driver.findElement(username).sendKeys(user);
    }

    // Enter password
    public void enterPassword(String pass) {
        driver.findElement(password).sendKeys(pass);
    }

    // Click Login
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    // Complete login
    public void login(String user, String pass) {

        enterUsername(user);

        enterPassword(pass);

        clickLogin();
    }
}
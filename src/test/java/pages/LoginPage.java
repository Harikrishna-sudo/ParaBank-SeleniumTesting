package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    private WebDriverWait wait;

    // Username field
    private By usernameField =
            By.name("username");

    // Password field
    private By passwordField =
            By.name("password");

    // Login button
    private By loginButton =
            By.xpath("//input[@value='Log In']");

    // Constructor
    public LoginPage(WebDriver driver) {

        this.driver = driver;

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        );
    }

    // Enter username
    public void enterUsername(String username) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField)
        ).clear();

        driver.findElement(usernameField)
                .sendKeys(username);
    }

    // Enter password
    public void enterPassword(String password) {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField)
        ).clear();

        driver.findElement(passwordField)
                .sendKeys(password);
    }

    // Click Login
    public void clickLogin() {

        wait.until(
                ExpectedConditions.elementToBeClickable(loginButton)
        ).click();
    }

    // Complete login
    public void login(String username, String password) {

        enterUsername(username);

        enterPassword(password);

        clickLogin();
    }
}
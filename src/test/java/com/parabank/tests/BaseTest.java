package com.parabank.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "https://parabank.parasoft.com/parabank";

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    protected void registerIfNotExists(String firstName, String lastName, String address, String city,
                                       String state, String zipCode, String phone, String ssn,
                                       String username, String password) {
        driver.get(BASE_URL + "/register.htm");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer.firstName"))).sendKeys(firstName);
        driver.findElement(By.id("customer.lastName")).sendKeys(lastName);
        driver.findElement(By.id("customer.address.street")).sendKeys(address);
        driver.findElement(By.id("customer.address.city")).sendKeys(city);
        driver.findElement(By.id("customer.address.state")).sendKeys(state);
        driver.findElement(By.id("customer.address.zipCode")).sendKeys(zipCode);
        driver.findElement(By.id("customer.phoneNumber")).sendKeys(phone);
        driver.findElement(By.id("customer.ssn")).sendKeys(ssn);
        driver.findElement(By.id("customer.username")).sendKeys(username);
        driver.findElement(By.id("customer.password")).sendKeys(password);
        driver.findElement(By.id("repeatedPassword")).sendKeys(password);
        driver.findElement(By.cssSelector("input[value='Register']")).click();

        // If username already exists error appears, registration was already done
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Log Out")));
        } catch (Exception ignored) {
        }
    }

    protected void login(String username, String password) {
        driver.get(BASE_URL + "/index.htm");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Attempt login
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(username);
            driver.findElement(By.name("password")).sendKeys(password);
            driver.findElement(By.cssSelector("input[value='Log In']")).click();
        } catch (Exception ignored) {
        }

        // Check if login succeeded by checking Log Out link
        boolean loggedIn = false;
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Log Out")));
            loggedIn = true;
        } catch (Exception ignored) {
        }

        // If not logged in, register user and log in
        if (!loggedIn) {
            registerIfNotExists("Navaneeth", "Inturi", "123 Main St", "Dallas",
                    "TX", "75001", "1234567890", "123-45-6789", username, password);

            if (driver.findElements(By.linkText("Log Out")).isEmpty()) {
                driver.get(BASE_URL + "/index.htm");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).sendKeys(username);
                driver.findElement(By.name("password")).sendKeys(password);
                driver.findElement(By.cssSelector("input[value='Log In']")).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText("Log Out")));
            }
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

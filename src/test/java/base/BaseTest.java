package base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setUp() {

        driver = new ChromeDriver();

        driver.manage().window().maximize();

        // Open ParaBank
        driver.get("https://parabank-17m8.onrender.com/parabank/index.htm");

        // Login
        driver.findElement(By.name("username"))
                .sendKeys("john");

        driver.findElement(By.name("password"))
                .sendKeys("demo");

        driver.findElement(By.xpath("//input[@value='Log In']"))
                .click();
    }

    @AfterEach
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogoutAndNavigationTest {
    static ChromeDriver driver;
    static String baseUrl="https://parabank-17m8.onrender.com/parabank";


    @BeforeAll
    static void setup() throws InterruptedException {
        driver=new ChromeDriver();
        driver.get(baseUrl);

        WebElement username=driver.findElement(By.name("username"));
        WebElement password=driver.findElement(By.name("password"));
        WebElement loginBtn=driver.findElement(By.xpath("//*[@type=\"submit\"]"));

        username.sendKeys("john");
        password.sendKeys("demo");
//        Thread.sleep(2000);
        loginBtn.click();

        String title=driver.getTitle();
        assertTrue(title.contains("Overview"));
    }

    @ParameterizedTest(name="[{index}] {0}")
    @DisplayName("Logged In Navigaton Test")
    @Order(1)
    @CsvSource({
            "/openaccount.htm, ParaBank | Open Account",
            "/overview.htm, ParaBank | Accounts Overview",
            "/transfer.htm, ParaBank | Transfer Funds",
            "/billpay.htm, ParaBank | Bill Pay",
            "/findtrans.htm, ParaBank | Find Transactions",
            "/updateprofile.htm, ParaBank | Update Profile",
            "/requestloan.htm, ParaBank | Loan Request"
    })
    void loggedInNavigationTest(String url,String expectedTitle){
        driver.navigate().to(baseUrl+url);
        String actualTitle=driver.getTitle();
        assertEquals(actualTitle,expectedTitle);
    }




    @Test
    @DisplayName("Logout Test")
    @Order(2)
    void afterLogoutStateTest(){

        WebElement logoutBtn=driver.findElement(By.xpath("//a[@href='logout.htm']"));
        logoutBtn.click();

        String title=driver.getTitle();
        assertEquals("ParaBank | Welcome | Online Banking",title);

    }

    @ParameterizedTest(name="[{index}] {0}")
    @DisplayName("Logged Out Navigation Test")
    @ValueSource(strings = {
            "/openaccount.htm",
            "/overview.htm",
            "/transfer.htm",
            "/billpay.htm",
            "/findtrans.htm",
            "/updateprofile.htm",
            "/requestloan.htm"
    })
    @Order(3)
    void afterLogoutNavigationTest(String url){
        driver.navigate().to(baseUrl+url);
        String title=driver.getTitle();
        assertEquals("ParaBank | Error",title);
    }

    @AfterAll
    static void destroy(){
        driver.quit();
    }
}

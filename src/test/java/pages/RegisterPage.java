package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "customer.firstName")       private WebElement firstName;
    @FindBy(id = "customer.lastName")        private WebElement lastName;
    @FindBy(id = "customer.address.street")  private WebElement address;
    @FindBy(id = "customer.address.city")    private WebElement city;
    @FindBy(id = "customer.address.state")   private WebElement state;
    @FindBy(id = "customer.address.zipCode") private WebElement zipCode;
    @FindBy(id = "customer.phoneNumber")     private WebElement phone;
    @FindBy(id = "customer.ssn")             private WebElement ssn;
    @FindBy(id = "customer.username")        private WebElement username;
    @FindBy(id = "customer.password")        private WebElement password;
    @FindBy(id = "repeatedPassword")         private WebElement confirmPassword;
    @FindBy(css = "input[value='Register']") private WebElement registerBtn;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("https://parabank.parasoft.com/parabank/register.htm");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer.firstName")));
    }

    public void fillForm(String first, String last, String addr, String cty,
                         String st, String zip, String ph, String ssnVal,
                         String user, String pass) {
        wait.until(ExpectedConditions.visibilityOf(firstName));
        firstName.clear();       firstName.sendKeys(first);
        lastName.clear();        lastName.sendKeys(last);
        address.clear();         address.sendKeys(addr);
        city.clear();            city.sendKeys(cty);
        state.clear();           state.sendKeys(st);
        zipCode.clear();         zipCode.sendKeys(zip);
        phone.clear();           phone.sendKeys(ph);
        ssn.clear();             ssn.sendKeys(ssnVal);
        username.clear();        username.sendKeys(user);
        password.clear();        password.sendKeys(pass);
        confirmPassword.clear(); confirmPassword.sendKeys(pass);
    }

    public void clickRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(registerBtn)).click();
    }

    public boolean isRegistrationSuccessful() {
        try {
            WebElement panel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("rightPanel")));
            String text = panel.getText();
            if (text.contains("Your account was created successfully") ||
                text.contains("Welcome") ||
                text.contains("was opened")) {
                return true;
            }
        } catch (Exception ignored) {}

        try {
            return driver.findElement(By.linkText("Log Out")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public String getUsernameTakenError() {
        try {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("customer.username.errors")));
            return el.getText().trim();
        } catch (Exception ignored) {}

        try {
            WebElement el = driver.findElement(By.xpath(
                    "//span[contains(text(),'username already exists') or " +
                    "contains(text(),'This username already exists')]"));
            if (el.isDisplayed()) return el.getText().trim();
        } catch (Exception ignored) {}

        return "";
    }

    public String getFieldError(String fieldId) {
        
        try {
            WebElement el = driver.findElement(By.id(fieldId + ".errors"));
            if (el.isDisplayed()) return el.getText().trim();
        } catch (Exception ignored) {}
        return "";
    }
}

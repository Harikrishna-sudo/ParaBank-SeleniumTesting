package tests;

import base.BaseTest;
import pages.RegisterPage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationTest extends BaseTest {

    private RegisterPage registerPage;

    private static final String UNIQUE_USER =
            "u" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 12);

    @BeforeEach
    public void goToRegister() {
        registerPage = new RegisterPage(driver);
        registerPage.open();
    }

    // ── TC-REG-001 ──
    @Test
    @Order(1)
    @DisplayName("TS004 - Successful registration with valid data")
    public void testSuccessfulRegistration() {
        registerPage.fillForm(
                "Helen", "Test",
                "456 Elm St", "Austin", "TX", "73301",
                "5125550101", "987-65-4321",
                UNIQUE_USER, "Password1!"
        );
        registerPage.clickRegister();

        assertTrue(registerPage.isRegistrationSuccessful(),
                "Expected success message or Log Out link after registration");
    }

    // ── TC-REG-002 ──
    @Test
    @Order(2)
    @DisplayName("TS005 - Registration fails when username already exists")
    public void testDuplicateUsernameRejected() {
        registerPage.fillForm(
                "John", "Doe",
                "1 Demo Rd", "Springfield", "IL", "62701",
                "2175550199", "111-22-3333",
                "john", "demo"
        );
        registerPage.clickRegister();

        String error = registerPage.getUsernameTakenError();
        assertFalse(error.isEmpty(),
                "Expected a 'username already exists' error message but got none");
    }

    // ── TC-REG-003 ──
    @Test
    @Order(3)
    @DisplayName("TS006 - Registration fails when required fields are empty")
    public void testEmptyFieldsShowValidationErrors() {
        registerPage.clickRegister();

        String firstNameError = registerPage.getFieldError("customer.firstName");
        String lastNameError  = registerPage.getFieldError("customer.lastName");
        String usernameError  = registerPage.getFieldError("customer.username");
        String passwordError  = registerPage.getFieldError("customer.password");

        assertFalse(firstNameError.isEmpty(),
                "First name validation error should appear");
        assertFalse(lastNameError.isEmpty(),
                "Last name validation error should appear");
        assertFalse(usernameError.isEmpty(),
                "Username validation error should appear");
        assertFalse(passwordError.isEmpty(),
                "Password validation error should appear");
    }
}

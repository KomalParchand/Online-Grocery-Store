package com.slipkart.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.UUID;

/**
 * Automated Test Suite for SlipKART Registration and Login Functionality
 * 
 * This test suite covers the core registration and login flows identified
 * in the functional test plan. Tests are designed to be independent and
 * can be run in any order.
 * 
 * Prerequisites:
 * - ChromeDriver in system PATH or set via system property
 * - Application running on configured base URL
 * - Database accessible and properly configured
 * - TestNG dependency in classpath
 */
public class RegistrationLoginAutomationTests {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080/Online/WebContent/";
    private static final int TIMEOUT_SECONDS = 10;
    
    // Test data - using unique identifiers to avoid conflicts
    private String testUsername;
    private String testEmail;
    
    @BeforeMethod
    public void setUp() {
        // Initialize WebDriver (ensure ChromeDriver is in PATH)
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT_SECONDS));
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        
        // Generate unique test data for each test run
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        testUsername = "testuser" + uniqueId;
        testEmail = "test" + uniqueId + "@email.com";
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    /**
     * Test Case CR-001: Successful Customer Registration with Complete Information
     * Validates the complete customer registration flow with all required data
     */
    @Test(priority = 1, description = "Customer Registration - Complete Information")
    public void testSuccessfulCustomerRegistration() {
        // Navigate to customer registration page
        driver.get(BASE_URL + "Custegistration.jsp");
        
        // Verify page title and header
        Assert.assertTrue(driver.getTitle().contains("Customer Registration"), 
                         "Registration page title not found");
        
        // Fill personal information
        fillPersonalInformation("John", "Doe", testUsername, "password123", testEmail);
        
        // Fill permanent address
        fillPermanentAddress("123 Main St", "Chicago", "Illinois", "60601");
        
        // Fill delivery address  
        fillDeliveryAddress("456 Oak Ave", "Houston", "Texas", "77001");
        
        // Fill credit card information
        fillCreditCardInfo("1234567890123456", "123", "2025", "Jan", "01");
        
        // Submit the form
        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit']"));
        submitButton.click();
        
        // Verify successful registration - should redirect to customer home page
        wait.until(ExpectedConditions.urlContains("Customerhome.jsp"));
        Assert.assertTrue(driver.getCurrentUrl().contains("Customerhome.jsp"), 
                         "User not redirected to customer home page");
        
        // Verify user is logged in
        WebElement loggedInText = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Logged in as')]")));
        Assert.assertTrue(loggedInText.getText().contains(testUsername), 
                         "User not properly logged in");
        
        // Verify navigation menu is present
        Assert.assertTrue(driver.findElement(By.linkText("HomePage")).isDisplayed(), 
                         "HomePage link not found");
        Assert.assertTrue(driver.findElement(By.linkText("Shopping Cart")).isDisplayed(), 
                         "Shopping Cart link not found");
        Assert.assertTrue(driver.findElement(By.linkText("Logout")).isDisplayed(), 
                         "Logout link not found");
    }
    
    /**
     * Test Case CL-001: Successful Customer Login
     * Tests login with valid credentials after registration
     */
    @Test(priority = 2, description = "Customer Login - Valid Credentials", 
          dependsOnMethods = {"testSuccessfulCustomerRegistration"})
    public void testSuccessfulCustomerLogin() {
        // First logout if any existing session
        logoutIfLoggedIn();
        
        // Navigate to login page
        driver.get(BASE_URL + "customer_login.jsp");
        
        // Verify login page elements
        Assert.assertTrue(driver.findElement(By.name("txtUName")).isDisplayed(), 
                         "Username field not found");
        Assert.assertTrue(driver.findElement(By.name("txtPassword")).isDisplayed(), 
                         "Password field not found");
        
        // Enter credentials
        driver.findElement(By.name("txtUName")).sendKeys(testUsername);
        driver.findElement(By.name("txtPassword")).sendKeys("password123");
        
        // Submit login form
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Verify successful login
        wait.until(ExpectedConditions.urlContains("Customerhome.jsp"));
        Assert.assertTrue(driver.getCurrentUrl().contains("Customerhome.jsp"), 
                         "Login failed - not redirected to home page");
        
        // Verify user session
        WebElement loggedInText = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Logged in as')]")));
        Assert.assertTrue(loggedInText.getText().contains(testUsername), 
                         "User session not established properly");
    }
    
    /**
     * Test Case CL-002: Login with Invalid Username
     * Tests login behavior with non-existent username
     */
    @Test(priority = 3, description = "Customer Login - Invalid Username")
    public void testLoginWithInvalidUsername() {
        // Navigate to login page
        driver.get(BASE_URL + "customer_login.jsp");
        
        // Enter invalid credentials
        driver.findElement(By.name("txtUName")).sendKeys("nonexistentuser");
        driver.findElement(By.name("txtPassword")).sendKeys("anypassword");
        
        // Submit login form
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Verify login failure - should redirect to invalid login page
        wait.until(ExpectedConditions.urlContains("invalidLogin.jsp"));
        Assert.assertTrue(driver.getCurrentUrl().contains("invalidLogin.jsp"), 
                         "Invalid login not handled properly");
    }
    
    /**
     * Test Case CL-003: Login with Invalid Password
     * Tests login behavior with correct username but wrong password
     */
    @Test(priority = 4, description = "Customer Login - Invalid Password", 
          dependsOnMethods = {"testSuccessfulCustomerRegistration"})
    public void testLoginWithInvalidPassword() {
        // Navigate to login page
        driver.get(BASE_URL + "customer_login.jsp");
        
        // Enter valid username with wrong password
        driver.findElement(By.name("txtUName")).sendKeys(testUsername);
        driver.findElement(By.name("txtPassword")).sendKeys("wrongpassword");
        
        // Submit login form
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Verify login failure
        wait.until(ExpectedConditions.urlContains("invalidLogin.jsp"));
        Assert.assertTrue(driver.getCurrentUrl().contains("invalidLogin.jsp"), 
                         "Invalid password not handled properly");
    }
    
    /**
     * Test Case CR-003: Registration with Missing Required Fields
     * Tests form validation for required fields
     */
    @Test(priority = 5, description = "Customer Registration - Missing Required Fields")
    public void testRegistrationWithMissingRequiredFields() {
        // Navigate to registration page
        driver.get(BASE_URL + "Custegistration.jsp");
        
        // Try to submit empty form
        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit']"));
        submitButton.click();
        
        // Verify form doesn't submit (stays on same page)
        Assert.assertTrue(driver.getCurrentUrl().contains("Custegistration.jsp"), 
                         "Form submitted with missing required fields");
        
        // Test individual required fields
        testRequiredField("fname", "First name is required");
        testRequiredField("lname", "Last name is required");
        testRequiredField("username", "Username is required");
        testRequiredField("passwrd", "Password is required");
        testRequiredField("email", "Email is required");
    }
    
    /**
     * Test Case SR-001: Successful Staff Registration
     * Tests staff registration functionality
     */
    @Test(priority = 6, description = "Staff Registration - Complete Information")
    public void testSuccessfulStaffRegistration() {
        // Generate unique staff test data
        String staffUsername = "staff" + UUID.randomUUID().toString().substring(0, 8);
        
        // Navigate to staff registration page
        driver.get(BASE_URL + "Staffregistration.jsp");
        
        // Fill staff registration form
        driver.findElement(By.name("fname")).sendKeys("Jane");
        driver.findElement(By.name("lname")).sendKeys("Smith");
        driver.findElement(By.name("username")).sendKeys(staffUsername);
        driver.findElement(By.name("passwrd")).sendKeys("staffpass123");
        driver.findElement(By.name("job_title")).sendKeys("Store Manager");
        driver.findElement(By.name("salary")).sendKeys("50000");
        driver.findElement(By.name("street")).sendKeys("789 Business Blvd");
        
        // Select city and state
        Select citySelect = new Select(driver.findElement(By.name("city")));
        citySelect.selectByValue("Chicago");
        
        Select stateSelect = new Select(driver.findElement(By.name("state")));
        stateSelect.selectByValue("Illinois");
        
        driver.findElement(By.name("zipcode")).sendKeys("60602");
        
        // Submit form
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        
        // Verify successful registration - should redirect to staff home page
        wait.until(ExpectedConditions.urlContains("Staffhomepage.jsp"));
        Assert.assertTrue(driver.getCurrentUrl().contains("Staffhomepage.jsp"), 
                         "Staff registration failed - not redirected to staff home page");
    }
    
    /**
     * Test Case CL-005: SQL Injection Prevention
     * Tests login security against SQL injection attacks
     */
    @Test(priority = 7, description = "Security Test - SQL Injection Prevention")
    public void testSQLInjectionPrevention() {
        // Navigate to login page
        driver.get(BASE_URL + "customer_login.jsp");
        
        // Enter SQL injection payload
        driver.findElement(By.name("txtUName")).sendKeys("admin' OR '1'='1");
        driver.findElement(By.name("txtPassword")).sendKeys("password");
        
        // Submit login form
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        
        // Verify injection is prevented - should go to invalid login page
        wait.until(ExpectedConditions.urlContains("invalidLogin.jsp"));
        Assert.assertTrue(driver.getCurrentUrl().contains("invalidLogin.jsp"), 
                         "SQL injection not prevented - security vulnerability detected");
    }
    
    /**
     * Test Case SM-002: Session Termination on Logout
     * Tests logout functionality and session management
     */
    @Test(priority = 8, description = "Session Management - Logout", 
          dependsOnMethods = {"testSuccessfulCustomerLogin"})
    public void testLogoutFunctionality() {
        // First login
        loginAsTestUser();
        
        // Verify user is logged in
        Assert.assertTrue(driver.getCurrentUrl().contains("Customerhome.jsp"), 
                         "User not logged in");
        
        // Click logout
        driver.findElement(By.linkText("Logout")).click();
        
        // Verify logout (this depends on logout implementation)
        // If logout redirects to login page, verify that
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("customer_login.jsp"),
                ExpectedConditions.urlContains("index.jsp")
        ));
        
        // Try to access protected page directly
        driver.get(BASE_URL + "Customerhome.jsp");
        
        // Should be redirected or show login requirement
        // This test may need adjustment based on actual logout implementation
        Assert.assertFalse(driver.getPageSource().contains("Logged in as: " + testUsername), 
                          "Session not properly terminated after logout");
    }
    
    // Helper Methods
    
    private void fillPersonalInformation(String fname, String lname, String username, 
                                       String password, String email) {
        driver.findElement(By.name("fname")).sendKeys(fname);
        driver.findElement(By.name("lname")).sendKeys(lname);
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("passwrd")).sendKeys(password);
        driver.findElement(By.name("email")).sendKeys(email);
    }
    
    private void fillPermanentAddress(String street, String city, String state, String zip) {
        // Select permanent address radio button
        driver.findElement(By.xpath("//input[@name='atype'][@value='PERMANENT']")).click();
        
        driver.findElement(By.name("astreet")).sendKeys(street);
        
        Select citySelect = new Select(driver.findElement(By.name("acity")));
        citySelect.selectByValue(city);
        
        Select stateSelect = new Select(driver.findElement(By.name("astate")));
        stateSelect.selectByValue(state);
        
        driver.findElement(By.name("azipcode")).sendKeys(zip);
    }
    
    private void fillDeliveryAddress(String street, String city, String state, String zip) {
        // Select delivery address radio button
        driver.findElement(By.xpath("//input[@name='btype'][@value='DELIVERY']")).click();
        
        driver.findElement(By.name("bstreet")).sendKeys(street);
        
        Select citySelect = new Select(driver.findElement(By.name("bcity")));
        citySelect.selectByValue(city);
        
        Select stateSelect = new Select(driver.findElement(By.name("bstate")));
        stateSelect.selectByValue(state);
        
        driver.findElement(By.name("bzipcode")).sendKeys(zip);
    }
    
    private void fillCreditCardInfo(String cardNumber, String cvv, String year, 
                                  String month, String day) {
        driver.findElement(By.name("creditcard_num")).sendKeys(cardNumber);
        driver.findElement(By.name("cvv")).sendKeys(cvv);
        
        Select yearSelect = new Select(driver.findElement(By.name("year")));
        yearSelect.selectByValue(year);
        
        Select monthSelect = new Select(driver.findElement(By.name("month")));
        monthSelect.selectByValue(month);
        
        Select daySelect = new Select(driver.findElement(By.name("day")));
        daySelect.selectByValue(day);
    }
    
    private void testRequiredField(String fieldName, String errorMessage) {
        // Clear form and fill only one field, then submit
        driver.navigate().refresh();
        
        WebElement field = driver.findElement(By.name(fieldName));
        field.clear();
        
        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit']"));
        submitButton.click();
        
        // Verify validation prevents submission
        Assert.assertTrue(driver.getCurrentUrl().contains("Custegistration.jsp"), 
                         errorMessage);
    }
    
    private void loginAsTestUser() {
        driver.get(BASE_URL + "customer_login.jsp");
        driver.findElement(By.name("txtUName")).sendKeys(testUsername);
        driver.findElement(By.name("txtPassword")).sendKeys("password123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("Customerhome.jsp"));
    }
    
    private void logoutIfLoggedIn() {
        try {
            driver.get(BASE_URL + "Customerhome.jsp");
            if (driver.getPageSource().contains("Logout")) {
                driver.findElement(By.linkText("Logout")).click();
            }
        } catch (Exception e) {
            // User not logged in, continue
        }
    }
}
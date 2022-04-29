package io.cucumber.parabank;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.Instant;

import static org.testng.AssertJUnit.assertEquals;


public class StepDefinitions {
    // Chrome Webdriver that will be used in every method
    private final WebDriver wd = new ChromeDriver();

    private String savedUserAndPassword = "";
    private String savedSSN = "";

    @Given("The Parabank website is opened")
    public void The_Parabank_website_is_opened() {
        //Open parabank homepage
        wd.get("https://parabank.parasoft.com/parabank/index.htm");
    }

    @When("I register with a new user")
    public void I_login_with_a_unique_user() throws InterruptedException {
        // Click on Register hyperlink
        wd.findElement(By.linkText("Register")).click();
        // Use epoch time to create a unique account
        long time = Instant.now().getEpochSecond();

        //Fill in details to register
        wd.findElement(By.id("customer.firstName")).sendKeys("first");
        wd.findElement(By.id("customer.lastName")).sendKeys("last");
        wd.findElement(By.id("customer.address.street")).sendKeys("1");
        wd.findElement(By.id("customer.address.city")).sendKeys("Cardiff");
        wd.findElement(By.id("customer.address.state")).sendKeys("Vale of Glamorgan");
        wd.findElement(By.id("customer.address.zipCode")).sendKeys("CF14 3UZ");
        // SSN is set to epoch time
        wd.findElement(By.id("customer.ssn")).sendKeys(String.valueOf(time));
        Thread.sleep(500);
        // username and password are set to "j" + epoch time
        wd.findElement(By.id("customer.username")).sendKeys("j" + time);
        savedUserAndPassword = "j" + time;
        // SSN is saved as a variable to be used when retrieving forgotten details
        savedSSN =String.valueOf(time);
        Thread.sleep(500);
        wd.findElement(By.id("customer.password")).sendKeys("j" + time);
        Thread.sleep(500);
        wd.findElement(By.id("repeatedPassword")).sendKeys("j" + time);
        Thread.sleep(500);
        // Click Register button
        wd.findElement(By.xpath("//input[@value=\"Register\"]")).click();
        Thread.sleep(3000);

    }

    @When("I register an empty user")
    public void I_register_an_empty_user() {
        // Click on Register link
        wd.findElement(By.linkText("Register")).click();
        // Click on Register button
        wd.findElement(By.xpath("//input[@value=\"Register\"]")).click();
    }

    @Then("Error messages should be present")
    public void Error_message_should_be_present() {
        // Webdriver checks that the relevant fields are displaying error messages
        wd.findElement(By.id("customer.firstName.errors")).isDisplayed();
        wd.findElement(By.id("customer.lastName.errors")).isDisplayed();
        wd.findElement(By.id("customer.address.street.errors")).isDisplayed();
        wd.findElement(By.id("customer.address.city.errors")).isDisplayed();
        wd.findElement(By.id("customer.address.state.errors")).isDisplayed();
        wd.findElement(By.id("customer.address.zipCode.errors")).isDisplayed();
        wd.findElement(By.id("customer.ssn.errors")).isDisplayed();
        wd.findElement(By.id("customer.username.errors")).isDisplayed();
        wd.findElement(By.id("customer.password.errors")).isDisplayed();
        wd.findElement(By.id("repeatedPassword.errors")).isDisplayed();
    }


    @When("I login with user {string} and password {string}")
    public void I_login_with_user(String user, String password) {
        // Webdriver logins to an account using a provided username and password
        wd.findElement(By.xpath("//input[@name=\"username\"]")).sendKeys(user);
        wd.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(password);
        wd.findElement(By.xpath("//input[@value=\"Log In\"]")).click();
    }

    @Then("I open a new savings account")
    public void I_open_a_new_savings_account() throws InterruptedException {
        // Open New Account link is clicked
        wd.findElement(By.linkText("Open New Account")).click();
        Thread.sleep(500);
        // SAVINGS account is selected from the drop down menu
        Select accountType = new Select(wd.findElement(By.id("type")));
        Thread.sleep(500);
        accountType.selectByVisibleText("SAVINGS");
        Thread.sleep(500);
        wd.findElement(By.xpath("//input")).click();
        Thread.sleep(500);

        wd.findElement(By.id("newAccountId")).click();
        Thread.sleep(500);
        //WebElements are created to check the account type and balance are correct
        WebElement savingsAccount = wd.findElement(By.id("accountType"));
        WebElement balance = wd.findElement(By.id("balance"));
        assertEquals(savingsAccount.getText(), "SAVINGS");
        assertEquals(balance.getText(), "$100.00");
    }

    @Then("The Account Overview is correct")
    public void The_Account_Overview_is_correct() throws InterruptedException {
        // Webdriver clicks the Accounts Overview link
        wd.findElement(By.linkText("Accounts Overview")).click();
        Thread.sleep(500);
        //Select the first account on the Overview page
        wd.findElement(By.xpath("//div[3]//tr[1]//a[1]")).click();
        Thread.sleep(500);

        // Create WebElements to check the correct amount is in the Checking account
        WebElement checkingAccount = wd.findElement(By.id("accountType"));
        WebElement balanceChecking = wd.findElement(By.id("balance"));
        assertEquals(checkingAccount.getText(), "CHECKING");
        assertEquals(balanceChecking.getText(), "$415.50");
    }

    @When("I transfer {string} from one account to another")
    public void I_transfer_from_one_account_to_another(String amount) throws InterruptedException {
        // Webdriver clicks the Transfer Funds link
        wd.findElement(By.linkText("Transfer Funds")).click();
        Thread.sleep(500);
        // Webdriver enters an amount provided
        wd.findElement(By.id("amount")).sendKeys(amount);
        Thread.sleep(500);
        Select accountNo = new Select(wd.findElement(By.id("toAccountId")));
        Thread.sleep(500);
        // Second account on the drop down menu is selected as the receiving account
        accountNo.selectByIndex(1);
        Thread.sleep(500);
        // Webdriver clicks to transfer funds
        wd.findElement(By.xpath("//input[@type=\"submit\"]")).click();
        Thread.sleep(1000);

        // check that the Transfer Complete! header is present
        new WebDriverWait(wd, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Transfer Complete!')]")));
    }

    @Then("The transfer has been successful")
    public void The_transfer_has_been_successful() throws InterruptedException {
        // Webdriver accesses the Accounts Overview screen
        wd.findElement(By.linkText("Accounts Overview")).click();
        Thread.sleep(500);
        // WebElements are created to check the correct amounts are in each account
        WebElement balance1 = wd.findElement(By.xpath("//div[3]//tr[1]//td[2]"));
        WebElement balance2 = wd.findElement(By.xpath("//div[3]//tr[2]//td[2]"));
        assertEquals(balance1.getText(), "$373.50");
        assertEquals(balance2.getText(), "$142.00");

    }

    @When("I have forgotten my login details")
    public void I_have_forgotten_my_login_details() throws InterruptedException {
        // Unique user is created and SSN saved
        I_login_with_a_unique_user();
        // User is logged out
        wd.findElement(By.linkText("Log Out")).click();
        // Webdriver clicks the Forgot login info? link
        wd.findElement(By.linkText("Forgot login info?")).click();
        Thread.sleep(500);
        // Webdriver enters details, most importantly the SSN which was saved beforehand, and is tied to an account
        wd.findElement(By.id("firstName")).sendKeys("first");
        wd.findElement(By.id("lastName")).sendKeys("last");
        wd.findElement(By.id("address.street")).sendKeys("1");
        wd.findElement(By.id("address.city")).sendKeys("Cardiff");
        wd.findElement(By.id("address.state")).sendKeys("Vale of Glamorgan");
        wd.findElement(By.id("address.zipCode")).sendKeys("CF14 3UZ");
        wd.findElement(By.id("ssn")).sendKeys(savedSSN);
        Thread.sleep(500);
        wd.findElement(By.xpath("//input[@value=\"Find My Login Info\"]")).click();
    }

    @Then("I can retrieve my details and login")
    public void I_can_retrieve_my_details_and_login() throws InterruptedException {
        // Title of page is checked
        checkTitle("ParaBank | Customer Lookup");
        Thread.sleep(500);
        // Username and Password are extracted from the <p></p> and added to an array
        String[] details = wd.findElement(By.xpath("//p[2]")).getText().split("[:\n]");
        // details are added to variables and trimmed of spaces
        String username = details[1].trim();
        String password = details[3].trim();
        Thread.sleep(500);
        // Webdriver logs the user out
        wd.findElement(By.linkText("Log Out")).click();
        Thread.sleep(500);
        // Webdriver logs the user in wiht the username and password provided
        I_login_with_user(username, password);
        checkTitle("ParaBank | Accounts Overview");
    }

    @Then("The page title should start with {string}")
    public void checkTitle(String titleStartsWith) {
        // Wait for the page to load timeout after ten seconds
        // Check that the page title is as expected
        new WebDriverWait(wd, Duration.ofSeconds(10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().equals(titleStartsWith);
            }
        });
    }

    @After()
    // Close browser window after finishing the test
    public void closeBrowser() {
        wd.quit();
    }
}

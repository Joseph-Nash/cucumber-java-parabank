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

import static org.testng.AssertJUnit.assertEquals;


public class StepDefinitions {

    private final WebDriver wd = new ChromeDriver();

    private String savedUserAndPassword = "";
    private String savedSSN = "";

    @Given("The Parabank website is opened")
    public void The_Parabank_website_is_opened() {
        wd.get("https://parabank.parasoft.com/parabank/index.htm");
    }

    @When("I register with a new user")
    public void I_login_with_a_unique_user() throws InterruptedException {
        // Click on Register hyperlink
        wd.findElement(By.linkText("Register")).click();

        int r = (int)(Math.random()*1000000);

        //Fill in details to register
        wd.findElement(By.id("customer.firstName")).sendKeys("first");
        wd.findElement(By.id("customer.lastName")).sendKeys("last");
        wd.findElement(By.id("customer.address.street")).sendKeys("1");
        wd.findElement(By.id("customer.address.city")).sendKeys("Cardiff");
        wd.findElement(By.id("customer.address.state")).sendKeys("Vale of Glamorgan");
        wd.findElement(By.id("customer.address.zipCode")).sendKeys("CF14 3UZ");
        wd.findElement(By.id("customer.ssn")).sendKeys(String.valueOf(r));
        Thread.sleep(500);
        wd.findElement(By.id("customer.username")).sendKeys("j" + r);
        savedUserAndPassword = "j" + r;
        savedSSN =String.valueOf(r);
        Thread.sleep(500);
        wd.findElement(By.id("customer.password")).sendKeys("j" + r);
        Thread.sleep(500);
        wd.findElement(By.id("repeatedPassword")).sendKeys("j" + r);
        Thread.sleep(500);
        // Click on Register button
        wd.findElement(By.xpath("//input[@value=\"Register\"]")).click();
        Thread.sleep(3000);

    }

    @When("I register an empty user")
    public void I_register_an_empty_user() {
        // Click on Register hyperlink
        wd.findElement(By.linkText("Register")).click();
        // Click on Register button
        wd.findElement(By.xpath("//input[@value=\"Register\"]")).click();
    }

    @Then("Error messages should be present")
    public void Error_message_should_be_present() {
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
        wd.findElement(By.xpath("//input[@name=\"username\"]")).sendKeys(user);
        wd.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(password);
        wd.findElement(By.xpath("//input[@value=\"Log In\"]")).click();
    }

    @Then("I open a new savings account")
    public void I_open_a_new_savings_account() throws InterruptedException {

        wd.findElement(By.linkText("Open New Account")).click();
        Thread.sleep(500);
        Select accountType = new Select(wd.findElement(By.id("type")));
        Thread.sleep(500);
        accountType.selectByVisibleText("SAVINGS");
        Thread.sleep(500);
        wd.findElement(By.xpath("//input")).click();
        Thread.sleep(500);

        wd.findElement(By.id("newAccountId")).click();
        Thread.sleep(500);
        WebElement savingsAccount = wd.findElement(By.id("accountType"));
        WebElement balance = wd.findElement(By.id("balance"));
        assertEquals(savingsAccount.getText(), "SAVINGS");
        assertEquals(balance.getText(), "$100.00");
    }

    @Then("The Account Overview is correct")
    public void The_Account_Overview_is_correct() throws InterruptedException {
        // remember to write comments

        wd.findElement(By.linkText("Accounts Overview")).click();
        Thread.sleep(500);
        wd.findElement(By.xpath("//div[3]//tr[1]//a[1]")).click();
        Thread.sleep(500);

        // remember to write comments
        WebElement checkingAccount = wd.findElement(By.id("accountType"));
        WebElement balanceChecking = wd.findElement(By.id("balance"));
        assertEquals(checkingAccount.getText(), "CHECKING");
        assertEquals(balanceChecking.getText(), "$415.50");
    }

    @When("I transfer {string} from one account to another")
    public void I_transfer_from_one_account_to_another(String amount) throws InterruptedException {
        wd.findElement(By.linkText("Transfer Funds")).click();
        Thread.sleep(500);
        wd.findElement(By.id("amount")).sendKeys(amount);
        Thread.sleep(500);
        Select accountNo = new Select(wd.findElement(By.id("toAccountId")));
        Thread.sleep(500);
        accountNo.selectByIndex(1);
        Thread.sleep(500);
        wd.findElement(By.xpath("//input[@type=\"submit\"]")).click();
        Thread.sleep(1000);

        // check that the Transfer Complete! header is present
        new WebDriverWait(wd, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(text(), 'Transfer Complete!')]")));
    }

    @Then("The transfer has been successful")
    public void The_transfer_has_been_successful() throws InterruptedException {
        wd.findElement(By.linkText("Accounts Overview")).click();
        Thread.sleep(500);
        WebElement balance1 = wd.findElement(By.xpath("//div[3]//tr[1]//td[2]"));
        WebElement balance2 = wd.findElement(By.xpath("//div[3]//tr[2]//td[2]"));
        assertEquals(balance1.getText(), "$373.50");
        assertEquals(balance2.getText(), "$142.00");

    }

    @When("I have forgotten my login details")
    public void I_have_forgotten_my_login_details() throws InterruptedException {
        I_login_with_a_unique_user();
        wd.findElement(By.linkText("Log Out")).click();

        wd.findElement(By.linkText("Forgot login info?")).click();
        Thread.sleep(500);
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
        checkTitle("ParaBank | Customer Lookup");
        Thread.sleep(500);
        String[] details = wd.findElement(By.xpath("//p[2]")).getText().split("[:\n]");
        String username = details[1].trim();
        String password = details[3].trim();
        Thread.sleep(500);
        wd.findElement(By.linkText("Log Out")).click();
        Thread.sleep(500);
        I_login_with_user(username, password);
        checkTitle("ParaBank | Accounts Overview");
    }

    @Then("The page title should start with {string}")
    public void checkTitle(String titleStartsWith) {
        // Google's search is rendered dynamically with JavaScript
        // Wait for the page to load timeout after ten seconds
        new WebDriverWait(wd, Duration.ofSeconds(10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().equals(titleStartsWith);
            }
        });
    }

    @After()
    public void closeBrowser() {
        wd.quit();
    }
}

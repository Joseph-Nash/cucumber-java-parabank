# Cucumber-Java Parabank

Parabank test suite was written using JDK11
Uses Cucumber framework with Selenium webdriver (Chrome).

## Get the code

Git:

    git clone https://github.com/Joseph-Nash/cucumber-java-parabank.git
    cd cucumber-java-parabank

## Running the Tests

Open a command line and run

    mvn test

This runs Cucumber features with the TestNG reporting framework. Reports can be accessed by entering the following in a command line.

    open target/surefire-reports/emailable-report.html

Thread.sleep(x milliseconds) has been added throughout to make it clearer what is being entered and clicked via the webdriver.
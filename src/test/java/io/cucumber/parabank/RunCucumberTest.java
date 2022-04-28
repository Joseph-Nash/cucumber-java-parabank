package io.cucumber.parabank;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
@CucumberOptions(
        features = {"src/test/resources/io/cucumber/parabank"},
        glue = {""},
        tags = ""
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
}

package com.xyzbank.base;

import com.xyzbank.models.Customer;
import com.xyzbank.pages.AccountPage;
import com.xyzbank.pages.HomePage;
import com.xyzbank.pages.ManagerDashboardPage;
import com.xyzbank.utils.DriverFactory;
import com.xyzbank.utils.TestDataFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Every test class extends this for a fresh browser per test method. Listeners
 * (AllureTestListener, AllureEnvironmentWriter) are wired via testng.xml, not here.
 */
public abstract class BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.getDriver();
        homePage = new HomePage(driver).open();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        LOGGER.info("Finished '{}' with status: {}", result.getName(), statusOf(result));
        DriverFactory.quitDriver();
    }

    /**
     * Shared precondition for every customer-facing test: a manager adds a customer, opens an
     * account for them, and the test logs in as that customer - all in-app (no hard navigation),
     * so the account created here is still there when the customer logs in.
     */
    @Step("Precondition: create a customer with a {currency} account and log in as them")
    protected AccountPage createCustomerWithAccountAndLogin(String currency) {
        Customer customer = TestDataFactory.uniqueValidCustomer();
        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        managerDashboard.openAddCustomerTab().addCustomer(customer);
        managerDashboard.openOpenAccountTab().openAccount(customer.fullName(), currency);

        return managerDashboard.goHome().goToCustomerLogin().loginAs(customer.fullName());
    }

    private String statusOf(ITestResult result) {
        return switch (result.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
    }
}

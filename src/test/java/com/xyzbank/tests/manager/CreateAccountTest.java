package com.xyzbank.tests.manager;

import com.xyzbank.base.BaseTest;
import com.xyzbank.models.Customer;
import com.xyzbank.pages.AccountPage;
import com.xyzbank.pages.ManagerDashboardPage;
import com.xyzbank.pages.OpenAccountPage;
import com.xyzbank.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Optional;

/** User Story 1 - Create Account acceptance criteria. */
@Epic("XYZ Bank")
@Feature("Manager - Customer Management")
public class CreateAccountTest extends BaseTest {

    @Test(description = "Manager can open an account for a customer that has already been added",
            groups = {"manager", "smoke"})
    @Story("Create Account")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Opening an account for an existing customer succeeds and returns a usable account number.")
    public void shouldCreateAccountForExistingCustomer() {
        Customer customer = addCustomer();

        ManagerDashboardPage managerDashboard = new ManagerDashboardPage(driver);
        Optional<String> alertMessage = managerDashboard.openOpenAccountTab().openAccount(customer.fullName(), "Dollar");

        Assert.assertTrue(alertMessage.isPresent(), "Expected an account-creation confirmation alert");
        Assert.assertTrue(OpenAccountPage.extractAccountNumber(alertMessage.get()).isPresent(),
                "Could not find an account number in confirmation: " + alertMessage.get());
    }

    @Test(description = "A customer with no account yet cannot access account features",
            groups = {"manager", "customer", "regression"})
    @Story("Create Account - Access Control")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Per the acceptance criteria, customers must not access their account until a manager has created one.")
    public void shouldBlockCustomerAccessBeforeAccountIsCreated() {
        Customer customer = addCustomer();

        AccountPage accountPage = new ManagerDashboardPage(driver).goHome()
                .goToCustomerLogin()
                .loginAs(customer.fullName());

        Assert.assertTrue(accountPage.hasNoAccountMessage(),
                "Customer without an account should see a prompt to open one, not account details");
    }

    @Step("Add a fresh customer as a precondition")
    private Customer addCustomer() {
        Customer customer = TestDataFactory.uniqueValidCustomer();
        homePage.goToManagerLogin().openAddCustomerTab().addCustomer(customer);
        return customer;
    }
}

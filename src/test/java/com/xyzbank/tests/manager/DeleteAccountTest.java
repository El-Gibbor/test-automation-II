package com.xyzbank.tests.manager;

import com.xyzbank.base.BaseTest;
import com.xyzbank.models.Customer;
import com.xyzbank.pages.CustomersListPage;
import com.xyzbank.pages.ManagerDashboardPage;
import com.xyzbank.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

/** User Story 1 - Delete Account acceptance criteria. */
@Epic("XYZ Bank")
@Feature("Manager - Customer Management")
public class DeleteAccountTest extends BaseTest {

    @Test(description = "Deleting a customer removes them from the Customers list and revokes account access",
            groups = {"manager", "customer", "smoke"})
    @Story("Delete Account")
    @Severity(SeverityLevel.CRITICAL)
    @Description("After a manager deletes a customer, the customer no longer appears in the manager's list "
            + "and can no longer log in to access the deleted account.")
    public void shouldDeleteCustomerAndRevokeAccess() {
        Customer customer = TestDataFactory.uniqueValidCustomer();

        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        managerDashboard.openAddCustomerTab().addCustomer(customer);
        managerDashboard.openOpenAccountTab().openAccount(customer.fullName(), "Dollar");

        CustomersListPage customersList = managerDashboard.openCustomersTab();
        customersList.deleteCustomer(customer.getFirstName(), customer.getLastName());

        Assert.assertFalse(customersList.isCustomerListed(customer.getFirstName(), customer.getLastName()),
                "Deleted customer should no longer appear in the Customers list");

        Assert.assertThrows(NoSuchElementException.class, () ->
                new ManagerDashboardPage(driver).goHome().goToCustomerLogin().loginAs(customer.fullName()));
    }
}

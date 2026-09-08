package com.xyzbank.tests.manager;

import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.xyzbank.base.BaseTest;
import com.xyzbank.jira.JiraLinks;
import com.xyzbank.models.Customer;
import com.xyzbank.pages.CustomersListPage;
import com.xyzbank.pages.ManagerDashboardPage;
import com.xyzbank.providers.DataProviders;
import com.xyzbank.utils.TestDataFactory;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Link;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/** User Story 1 - Add Customer acceptance criteria. */
@Epic("XYZ Bank")
@Feature("Manager - Customer Management")
@Link(name = "User Story 1", url = JiraLinks.USER_STORY_1)
@Link(name = "Xray Test Set", url = JiraLinks.MANAGER_TEST_SET)
public class AddCustomerTest extends BaseTest {

    @Test(description = "Manager can add a new customer with a valid, alphabetic name and numeric postal code",
            groups = {"manager", "smoke"}
)
    @Story("Adding Customers")
    @Severity(SeverityLevel.CRITICAL)
    @Description("A bank manager adds a customer with valid data; the customer is confirmed and appears in the Customers list.")
    public void shouldAddCustomerSuccessfully() {
        Customer customer = TestDataFactory.uniqueValidCustomer();

        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        Optional<String> alertMessage = managerDashboard.openAddCustomerTab().addCustomer(customer);

        Assert.assertTrue(alertMessage.isPresent(), "Expected a confirmation alert after adding a customer");
        Assert.assertTrue(alertMessage.get().contains("Customer added successfully"),
                "Unexpected confirmation message: " + alertMessage.get());

        CustomersListPage customersList = managerDashboard.openCustomersTab();
        Assert.assertTrue(customersList.isCustomerListed(customer.getFirstName(), customer.getLastName()),
                "Newly added customer should appear in the Customers list"
        );
    }

    @Test(description = "Manager cannot add a customer with a non-alphabetic name or a non-numeric postal code",
            dataProviderClass = DataProviders.class, dataProvider = "invalidCustomers",
            groups = {"manager", "regression"}
)
    @Story("Adding Customers - Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Issue(JiraLinks.DEFECT_ADD_CUSTOMER_VALIDATION)
    @Description("Names must be alphabetic-only and postal codes numeric-only per the acceptance criteria; "
            + "invalid input must not result in a new customer record. Non-blank, non-alphabetic/numeric "
            + "cases currently fail: see " + JiraLinks.DEFECT_ADD_CUSTOMER_VALIDATION + "."
)
    public void shouldRejectInvalidCustomer(Customer customer, String expectedError) {
        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        managerDashboard.openAddCustomerTab().addCustomer(customer);

        CustomersListPage customersList = managerDashboard.openCustomersTab();
        Assert.assertFalse(customersList.isCustomerListed(customer.getFirstName(), customer.getLastName()), expectedError);
    }

    @Test(description = "The Customers list search filters to only the matching customer",
            groups = {"manager", "regression"})
    @Story("Adding Customers - Customer List Search")
    @Severity(SeverityLevel.NORMAL)
    @Description("Searching by a newly added customer's name filters the list down to that one row, "
            + "and searching a term that matches nobody returns no rows."
    )
    public void shouldFilterCustomerListBySearch() {
        Customer customer = TestDataFactory.uniqueValidCustomer();
        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        managerDashboard.openAddCustomerTab().addCustomer(customer);

        CustomersListPage customersList = managerDashboard.openCustomersTab().search(customer.getFirstName());
        Assert.assertTrue(customersList.isCustomerListed(customer.getFirstName(), customer.getLastName()),
                "Search should still surface the customer being searched for");
        Assert.assertEquals(customersList.rowCount(), 1,
                "A search on a unique generated name should filter to exactly one row");

        customersList.search(java.util.UUID.randomUUID().toString());
        Assert.assertEquals(customersList.rowCount(), 0, "A search matching nobody should return no rows");
    }

    @Test(description = "An oversized but otherwise valid name is still accepted and listed correctly",
            groups = {"manager", "regression"})
    @Story("Adding Customers - Boundary Input")
    @Severity(SeverityLevel.MINOR)
    @Description("The acceptance criteria set no length limit on names; this pins the actual behavior "
            + "for an unusually long (but alphabetic) name so any future truncation is caught as a regression."
    )
    public void shouldAcceptOversizedAlphabeticName() {
        Customer customer = TestDataFactory.uniqueValidCustomer();
        customer.setFirstName(customer.getFirstName() + "z".repeat(100));

        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        managerDashboard.openAddCustomerTab().addCustomer(customer);

        CustomersListPage customersList = managerDashboard.openCustomersTab();
        Assert.assertTrue(customersList.isCustomerListed(customer.getFirstName(), customer.getLastName()),
                "An oversized alphabetic name should still be stored and displayed in full, not truncated");
    }

    @Test(description = "Submitting the same customer twice is rejected as a duplicate",
            groups = {"manager", "regression"})
    @Story("Adding Customers - Duplicate Handling")
    @Severity(SeverityLevel.NORMAL)
    @Description("The acceptance criteria do not explicitly cover duplicates, but the live app flags a "
            + "second submission of identical customer data and does not create a second record."
    )
    public void shouldRejectDuplicateCustomerSubmission() {
        Customer customer = TestDataFactory.uniqueValidCustomer();
        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        managerDashboard.openAddCustomerTab().addCustomer(customer);

        Optional<String> secondAttempt = managerDashboard.openAddCustomerTab().addCustomer(customer);
        Assert.assertTrue(secondAttempt.isPresent() && secondAttempt.get().toLowerCase().contains("duplicate"),
                "Expected the second submission to be flagged as a duplicate, got: " + secondAttempt);

        CustomersListPage customersList = managerDashboard.openCustomersTab().search(customer.getFirstName());
        Assert.assertEquals(customersList.rowCount(), 1,
                "Only one row should exist after a rejected duplicate submission");
    }
}

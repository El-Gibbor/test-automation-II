package com.xyzbank.tests.manager;

import java.util.Optional;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.xyzbank.base.BaseTest;
import com.xyzbank.models.Customer;
import com.xyzbank.pages.CustomersListPage;
import com.xyzbank.pages.ManagerDashboardPage;
import com.xyzbank.providers.DataProviders;
import com.xyzbank.utils.TestDataFactory;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

/** User Story 1 - Add Customer acceptance criteria. */
@Epic("XYZ Bank")
@Feature("Manager - Customer Management")
public class AddCustomerTest extends BaseTest {

    @Test(description = "Manager can add a new customer with a valid, alphabetic name and numeric postal code",
            groups = {"manager", "smoke"}
)
    @Story("Add Customer")
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
    @Story("Add Customer - Input Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Names must be alphabetic-only and postal codes numeric-only per the acceptance criteria; "
            + "invalid input must not result in a new customer record."
)
    public void shouldRejectInvalidCustomer(Customer customer, String expectedError) {
        ManagerDashboardPage managerDashboard = homePage.goToManagerLogin();
        managerDashboard.openAddCustomerTab().addCustomer(customer);

        CustomersListPage customersList = managerDashboard.openCustomersTab();
        Assert.assertFalse(customersList.isCustomerListed(customer.getFirstName(), customer.getLastName()), expectedError);
    }
}

package com.xyzbank.pages;

import com.xyzbank.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The manager landing page with the three tabs: Add Customer, Open Account, Customers. */
public class ManagerDashboardPage extends BasePage {

    private static final By ADD_CUSTOMER_TAB = By.cssSelector("button[ng-click='addCust()']");
    private static final By OPEN_ACCOUNT_TAB = By.cssSelector("button[ng-click='openAccount()']");
    private static final By CUSTOMERS_TAB = By.cssSelector("button[ng-click='showCust()']");

    public ManagerDashboardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open 'Add Customer' tab")
    public AddCustomerPage openAddCustomerTab() {
        click(ADD_CUSTOMER_TAB);
        return new AddCustomerPage(driver);
    }

    @Step("Open 'Open Account' tab")
    public OpenAccountPage openOpenAccountTab() {
        click(OPEN_ACCOUNT_TAB);
        return new OpenAccountPage(driver);
    }

    @Step("Open 'Customers' tab")
    public CustomersListPage openCustomersTab() {
        click(CUSTOMERS_TAB);
        return new CustomersListPage(driver);
    }
}

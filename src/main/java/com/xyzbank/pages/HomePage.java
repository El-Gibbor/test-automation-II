package com.xyzbank.pages;

import com.xyzbank.utils.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The landing page offering the two entry points into the app: customer vs. bank manager. */
public class HomePage extends BasePage {

    private static final By CUSTOMER_LOGIN_BUTTON = By.cssSelector("button[ng-click='customer()']");
    private static final By MANAGER_LOGIN_BUTTON = By.cssSelector("button[ng-click='manager()']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Navigate to the XYZ Bank home page")
    public HomePage open() {
        driver.get(ConfigReader.baseUrl());
        return this;
    }

    @Step("Go to Bank Manager Login")
    public ManagerDashboardPage goToManagerLogin() {
        click(MANAGER_LOGIN_BUTTON);
        return new ManagerDashboardPage(driver);
    }

    @Step("Go to Customer Login")
    public CustomerLoginPage goToCustomerLogin() {
        click(CUSTOMER_LOGIN_BUTTON);
        return new CustomerLoginPage(driver);
    }
}

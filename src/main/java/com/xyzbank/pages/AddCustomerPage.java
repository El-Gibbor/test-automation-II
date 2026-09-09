package com.xyzbank.pages;

import com.xyzbank.base.BasePage;
import com.xyzbank.models.Customer;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

/** "Add Customer" form under the manager dashboard. Success is confirmed via a JS alert. */
public class AddCustomerPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.cssSelector("input[ng-model='fName']");
    private static final By LAST_NAME_INPUT = By.cssSelector("input[ng-model='lName']");
    private static final By POST_CODE_INPUT = By.cssSelector("input[ng-model='postCd']");
    private static final By SUBMIT_BUTTON = By.cssSelector("form[name='myForm'] button[type='submit']");

    public AddCustomerPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Submits the form with the given customer and returns the alert message, if the app showed
     * one. Empty means the submission was silently accepted/rejected with no confirmation dialog -
     * callers decide what that implies for their scenario.
     */
    @Step("Submit 'Add Customer' form for {customer}")
    public Optional<String> addCustomer(Customer customer) {
        type(FIRST_NAME_INPUT, customer.getFirstName());
        type(LAST_NAME_INPUT, customer.getLastName());
        type(POST_CODE_INPUT, customer.getPostalCode());
        click(SUBMIT_BUTTON);
        return acceptAlert();
    }
}

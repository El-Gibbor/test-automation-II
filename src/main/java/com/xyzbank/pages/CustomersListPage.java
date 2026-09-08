package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** The manager's "Customers" tab: searchable table with a Delete action per row. */
public class CustomersListPage extends BasePage {

    private static final By SEARCH_INPUT = By.cssSelector("input[ng-model='searchCustomer']");
    private static final By TABLE_ROWS = By.cssSelector("table tbody tr");
    private static final By DELETE_BUTTON_IN_ROW = By.cssSelector("button[ng-click='deleteCust(cust)']");

    public CustomersListPage(WebDriver driver) {
        super(driver);
    }

    @Step("Search customers for '{term}'")
    public CustomersListPage search(String term) {
        type(SEARCH_INPUT, term);
        return this;
    }

    @Step("Check whether customer '{firstName} {lastName}' is listed")
    public boolean isCustomerListed(String firstName, String lastName) {
        return findAll(TABLE_ROWS).stream()
                .anyMatch(row -> row.getText().contains(firstName) && row.getText().contains(lastName));
    }

    /** Number of rows currently shown, i.e. after any {@link #search(String)} filtering has applied. */
    @Step("Count visible customer rows")
    public int rowCount() {
        return findAll(TABLE_ROWS).size();
    }

    @Step("Delete customer '{firstName} {lastName}'")
    public void deleteCustomer(String firstName, String lastName) {
        WebElement row = findAll(TABLE_ROWS).stream()
                .filter(r -> r.getText().contains(firstName) && r.getText().contains(lastName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Customer not found in list: " + firstName + " " + lastName));
        row.findElement(DELETE_BUTTON_IN_ROW).click();
    }
}

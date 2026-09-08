package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Read-only transaction history for the selected account. */
public class TransactionsPage extends BasePage {

    private static final By TRANSACTION_ROWS = By.cssSelector("table tbody tr");
    // no reset/clear/delete control exists anywhere on this view - verified by absence, not presence
    private static final By ANY_RESET_CONTROL = By.xpath(
            "//button[contains(translate(., 'RESET', 'reset'), 'reset') or contains(translate(., 'CLEAR', 'clear'), 'clear')]");

    public TransactionsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Count transactions listed")
    public int getTransactionCount() {
        return findAll(TRANSACTION_ROWS).size();
    }

    @Step("Verify no control exists to reset or clear transaction history")
    public boolean hasResetOrClearControl() {
        return isDisplayed(ANY_RESET_CONTROL);
    }
}

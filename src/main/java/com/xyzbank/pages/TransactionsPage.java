package com.xyzbank.pages;

import com.xyzbank.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/** Read-only transaction history for the selected account. */
public class TransactionsPage extends BasePage {

    private static final By TRANSACTION_ROWS = By.cssSelector("table tbody tr");
    // no reset/clear/delete control exists anywhere on this view - verified by absence, not presence
    private static final By ANY_RESET_CONTROL = By.xpath(
            "//button[contains(translate(., 'RESET', 'reset'), 'reset') or contains(translate(., 'CLEAR', 'clear'), 'clear')]");

    public TransactionsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Counts the rows currently rendered. Waits briefly for the first row to appear so a call
     * right after navigating here doesn't race Angular's render; a genuinely empty history just
     * lets the wait time out and falls through to the real (zero) count below.
     */
    @Step("Count transactions listed")
    public int getTransactionCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(TRANSACTION_ROWS));
        } catch (TimeoutException noRowsRendered) {
            // fall through - an empty transaction history is a valid outcome, not a failure
        }
        return findAll(TRANSACTION_ROWS).size();
    }

    @Step("Verify no control exists to reset or clear transaction history")
    public boolean hasResetOrClearControl() {
        return isDisplayed(ANY_RESET_CONTROL);
    }
}

package com.xyzbank.pages;

import com.xyzbank.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Deposit sub-view. Result is shown inline as text (e.g. "Deposit Successful"), not a JS alert.
 * Locators are scoped to this form's ng-submit="deposit()" rather than the generic
 * input[ng-model='amount'] - the Withdraw form uses the exact same ng-model/button markup, so an
 * unscoped locator can momentarily match the wrong form's (still-transitioning) element.
 */
public class DepositPage extends BasePage {

    private static final By FORM = By.cssSelector("form[ng-submit='deposit()']");
    private static final By AMOUNT_INPUT = By.cssSelector("form[ng-submit='deposit()'] input[ng-model='amount']");
    private static final By SUBMIT_BUTTON = By.cssSelector("form[ng-submit='deposit()'] button[type='submit']");
    private static final By RESULT_MESSAGE = By.xpath("//form[@ng-submit='deposit()']/preceding-sibling::span[contains(@class,'error')]");

    public DepositPage(WebDriver driver) {
        super(driver);
    }

    @Step("Deposit amount: {amount}")
    public String deposit(double amount) {
        return deposit(String.valueOf(amount));
    }

    /**
     * Overload for fixtures that need to type a non-numeric value (e.g. "abc"), which a double
     * can't represent. Waits for the result message to render before reading it, so a caller
     * never races Angular's digest cycle (e.g. checking the transaction list right after this
     * returns). If native browser validation blocks the submission outright (a decimal amount
     * with no matching `step`, for instance), no message ever appears and this returns "".
     */
    @Step("Deposit amount: {rawAmount}")
    public String deposit(String rawAmount) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORM));
        type(AMOUNT_INPUT, rawAmount);
        click(SUBMIT_BUTTON);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(RESULT_MESSAGE)).getText();
        } catch (TimeoutException noMessageShown) {
            return "";
        }
    }
}

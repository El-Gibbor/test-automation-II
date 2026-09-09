package com.xyzbank.pages;

import com.xyzbank.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Withdrawal sub-view. Result is shown inline as text ("Transaction successful" / "Transaction Failed...").
 * Locators are scoped to this form's ng-submit="withdrawl()" - see {@link DepositPage} for why an
 * unscoped input[ng-model='amount']/button[type='submit'] locator is not safe here.
 */
public class WithdrawPage extends BasePage {

    private static final By FORM = By.cssSelector("form[ng-submit='withdrawl()']");
    private static final By AMOUNT_INPUT = By.cssSelector("form[ng-submit='withdrawl()'] input[ng-model='amount']");
    private static final By SUBMIT_BUTTON = By.cssSelector("form[ng-submit='withdrawl()'] button[type='submit']");
    private static final By RESULT_MESSAGE = By.xpath("//form[@ng-submit='withdrawl()']/preceding-sibling::span[contains(@class,'error')]");

    public WithdrawPage(WebDriver driver) {
        super(driver);
    }

    @Step("Withdraw amount: {amount}")
    public String withdraw(double amount) {
        return withdraw(String.valueOf(amount));
    }

    /**
     * Overload for fixtures that need to type a non-numeric value (e.g. "abc"), which a double
     * can't represent. Waits for the result message to render before reading it, so a caller
     * never races Angular's digest cycle. If native browser validation blocks the submission
     * outright (a decimal amount with no matching `step`, for instance), no message ever
     * appears and this returns "".
     */
    @Step("Withdraw amount: {rawAmount}")
    public String withdraw(String rawAmount) {
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

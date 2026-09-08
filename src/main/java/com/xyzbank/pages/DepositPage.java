package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
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
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORM));
        type(AMOUNT_INPUT, String.valueOf(amount));
        click(SUBMIT_BUTTON);
        return isDisplayed(RESULT_MESSAGE) ? getText(RESULT_MESSAGE) : "";
    }
}

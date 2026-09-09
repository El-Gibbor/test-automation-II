package com.xyzbank.pages;

import com.xyzbank.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * The logged-in customer view: account summary plus the Transactions / Deposit / Withdrawal tabs.
 * If the customer has no account yet, {@link #hasNoAccountMessage()} is true and the tabs are absent.
 */
public class AccountPage extends BasePage {

    private static final By NO_ACCOUNT_MESSAGE = By.cssSelector("span[ng-show='noAccount']");
    private static final By ACCOUNT_SUMMARY_VALUES = By.xpath("//div[@ng-hide='noAccount' and contains(@class,'center')]/strong");
    private static final By TRANSACTIONS_TAB = By.cssSelector("button[ng-click='transactions()']");
    private static final By DEPOSIT_TAB = By.cssSelector("button[ng-click='deposit()']");
    private static final By WITHDRAW_TAB = By.cssSelector("button[ng-click='withdrawl()']");

    public AccountPage(WebDriver driver) {
        super(driver);
    }

    /** True when the app shows "Please open an account with us." instead of an account summary. */
    @Step("Verify customer has no account")
    public boolean hasNoAccountMessage() {
        return isDisplayed(NO_ACCOUNT_MESSAGE);
    }

    @Step("Read current account balance")
    public double getBalance() {
        List<org.openqa.selenium.WebElement> values = findAll(ACCOUNT_SUMMARY_VALUES);
        // order in the DOM is: [accountNumber, balance, currency]
        return Double.parseDouble(values.get(1).getText().trim());
    }

    @Step("Go to Deposit tab")
    public DepositPage goToDeposit() {
        click(DEPOSIT_TAB);
        return new DepositPage(driver);
    }

    @Step("Go to Withdrawal tab")
    public WithdrawPage goToWithdraw() {
        click(WITHDRAW_TAB);
        return new WithdrawPage(driver);
    }

    @Step("Go to Transactions tab")
    public TransactionsPage goToTransactions() {
        click(TRANSACTIONS_TAB);
        return new TransactionsPage(driver);
    }
}

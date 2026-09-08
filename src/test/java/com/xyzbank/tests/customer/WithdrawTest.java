package com.xyzbank.tests.customer;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.xyzbank.base.BaseTest;
import com.xyzbank.jira.JiraLinks;
import com.xyzbank.models.TransactionAmount;
import com.xyzbank.pages.AccountPage;
import com.xyzbank.providers.DataProviders;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

/** User Story 2 - Withdrawing Money acceptance criteria. */
@Epic("XYZ Bank")
@Feature("Customer - Transactions")
@Link(name = "User Story 2", url = JiraLinks.USER_STORY_2)
@Link(name = "Xray Test Set", url = JiraLinks.CUSTOMER_TEST_SET)
public class WithdrawTest extends BaseTest {

    private static final double SEED_BALANCE = 1000;

    @Test(description = "Withdrawal amount is validated against positivity and available balance",
            dataProviderClass = DataProviders.class, dataProvider = "withdrawAmounts",
            groups = {"customer", "regression"})
    @Story("Withdrawing Money")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Valid, in-balance amounts are debited; zero/negative amounts and amounts exceeding the balance must be rejected.")
    public void shouldHandleWithdrawalAmount(TransactionAmount data) {
        AccountPage accountPage = createCustomerWithAccountAndLogin("Dollar");
        seedBalance(accountPage);
        double balanceBefore = new AccountPage(driver).getBalance();

        String message = new AccountPage(driver).goToWithdraw().withdraw(data.inputValue());
        double balanceAfter = new AccountPage(driver).getBalance();

        if (data.isExpectedValid()) {
            Assert.assertEquals(message, "Transaction successful", data.getDescription());
            Assert.assertEquals(balanceAfter, balanceBefore - data.getAmount(), 0.001, "Balance should decrease by the withdrawn amount");
        } else {
            Assert.assertNotEquals(message, "Transaction successful", data.getDescription());
            Assert.assertEquals(balanceAfter, balanceBefore, 0.001, "Balance must be unaffected by a rejected withdrawal");
        }
    }

    @Step("Seed the account with a known starting balance")
    private void seedBalance(AccountPage accountPage) {
        accountPage.goToDeposit().deposit(SEED_BALANCE);
    }
}

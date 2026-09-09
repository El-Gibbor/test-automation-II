package com.xyzbank.tests.customer;

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
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

// Updtatnfkmsdnfds

/** User Story 2 - Depositing Funds acceptance criteria. */
@Epic("XYZ Bank")
@Feature("Customer - Transactions")
@Link(name = "User Story 2", url = JiraLinks.USER_STORY_2)
@Link(name = "Xray Test Set", url = JiraLinks.CUSTOMER_TEST_SET)
public class DepositTest extends BaseTest {

    @Test(description = "Deposit amount is validated and, when valid, credited to the account balance",
            dataProviderClass = DataProviders.class, dataProvider = "depositAmounts",
            groups = {"customer", "regression"}
    )
    @Story("Depositing Funds")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Positive amounts are accepted and increase the balance; zero/negative amounts must be rejected.")
    public void shouldHandleDepositAmount(TransactionAmount data) {
        AccountPage accountPage = createCustomerWithAccountAndLogin("Dollar");
        String message = accountPage.goToDeposit().deposit(data.inputValue());
        double balance = new AccountPage(driver).getBalance();

        if (data.isExpectedValid()) {
            Assert.assertEquals(message, "Deposit Successful", data.getDescription());
            Assert.assertEquals(balance, data.getAmount(), 0.001, "Balance should equal the deposited amount");
        } else {
            Assert.assertNotEquals(message, "Deposit Successful", data.getDescription());
            Assert.assertEquals(balance, 0.0, 0.001, "Balance must be unaffected by a rejected deposit");
        }
    }
}

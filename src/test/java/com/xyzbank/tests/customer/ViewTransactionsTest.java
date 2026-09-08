package com.xyzbank.tests.customer;

import com.xyzbank.base.BaseTest;
import com.xyzbank.pages.AccountPage;
import com.xyzbank.pages.TransactionsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

/** User Story 2 - Viewing Transactions + Transaction Security acceptance criteria. */
@Epic("XYZ Bank")
@Feature("Customer - Transactions")
public class ViewTransactionsTest extends BaseTest {

    @Test(description = "Customer can view their transaction history after making a deposit",
            groups = {"customer", "smoke"}
    )
    @Story("View Transactions")
    @Severity(SeverityLevel.NORMAL)
    @Description("A deposit made by the customer must appear as an entry in their transaction list.")
    public void shouldListTransactionAfterDeposit() {
        AccountPage accountPage = createCustomerWithAccountAndLogin("Dollar");
        accountPage.goToDeposit().deposit(250);

        TransactionsPage transactions = new AccountPage(driver).goToTransactions();
        Assert.assertEquals(transactions.getTransactionCount(), 1, "Expected exactly one transaction after one deposit");
    }

    @Test(description = "No control exists for a customer to reset or alter their transaction history",
            groups = {"customer", "regression"}
    )
    @Story("Transaction Security")
    @Severity(SeverityLevel.NORMAL)
    @Description("Per the acceptance criteria, customers must not be able to reset or alter their transaction history.")
    public void shouldNotExposeTransactionResetControl() {
        AccountPage accountPage = createCustomerWithAccountAndLogin("Dollar");
        TransactionsPage transactions = accountPage.goToTransactions();

        Assert.assertFalse(transactions.hasResetOrClearControl(),
                "No reset/clear control should be present on the transactions view"
        );
    }
}

package com.xyzbank.providers;

import com.xyzbank.models.Customer;
import com.xyzbank.models.TransactionAmount;
import com.xyzbank.utils.TestDataReader;
import org.testng.annotations.DataProvider;

import java.util.List;

/**
 * Central home for every TestNG @DataProvider. Tests reference these by name
 * (dataProviderClass = DataProviders.class) instead of reading JSON themselves.
 */
public final class DataProviders {

    private DataProviders() {
    }

    @DataProvider(name = "invalidCustomers")
    public static Object[][] invalidCustomers() {
        List<Customer> customers = TestDataReader.readList("invalid-customers.json", Customer.class);
        Object[][] rows = new Object[customers.size()][2];
        for (int i = 0; i < customers.size(); i++) {
            rows[i][0] = customers.get(i);
            rows[i][1] = customers.get(i).getExpectedError();
        }
        return rows;
    }

    @DataProvider(name = "depositAmounts")
    public static Object[][] depositAmounts() {
        return toRows(TestDataReader.readList("deposit-amounts.json", TransactionAmount.class));
    }

    @DataProvider(name = "withdrawAmounts")
    public static Object[][] withdrawAmounts() {
        return toRows(TestDataReader.readList("withdraw-amounts.json", TransactionAmount.class));
    }

    private static <T> Object[][] toRows(List<T> items) {
        Object[][] rows = new Object[items.size()][1];
        for (int i = 0; i < items.size(); i++) {
            rows[i][0] = items.get(i);
        }
        return rows;
    }
}

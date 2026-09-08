package com.xyzbank.models;

/**
 * Maps 1:1 to entries in testdata/deposit-amounts.json and testdata/withdraw-amounts.json.
 */
public class TransactionAmount {

    private double amount;
    private boolean expectedValid;
    private String description;

    public TransactionAmount() {
        // required by Jackson
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isExpectedValid() {
        return expectedValid;
    }

    public void setExpectedValid(boolean expectedValid) {
        this.expectedValid = expectedValid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        // used as the Allure/TestNG parameter label
        return description + " (amount=" + amount + ")";
    }
}

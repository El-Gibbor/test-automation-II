package com.xyzbank.models;

/**
 * Maps 1:1 to entries in testdata/deposit-amounts.json and testdata/withdraw-amounts.json.
 */
public class TransactionAmount {

    private double amount;
    private boolean expectedValid;
    private String description;
    // Only set for non-numeric-input fixtures (e.g. "abc"), since a numeric `amount` field can
    // never represent that value. When present, tests type this instead of `amount`.
    private String rawInput;

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

    public String getRawInput() {
        return rawInput;
    }

    public void setRawInput(String rawInput) {
        this.rawInput = rawInput;
    }

    /** What a page object should actually type: the raw override if set, else the numeric amount. */
    public String inputValue() {
        return rawInput != null ? rawInput : String.valueOf(amount);
    }

    @Override
    public String toString() {
        // used as the Allure/TestNG parameter label
        return description + " (amount=" + inputValue() + ")";
    }
}

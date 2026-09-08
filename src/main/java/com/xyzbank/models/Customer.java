package com.xyzbank.models;

/**
 * Maps 1:1 to entries in testdata/valid-customers.json and testdata/invalid-customers.json.
 * Kept as a plain POJO so Jackson can deserialize fixtures directly and tests can pass a
 * typed object (rather than raw strings) into page objects and Allure step messages.
 */
public class Customer {

    private String firstName;
    private String lastName;
    private String postalCode;
    private String description;
    private String expectedError; // populated only in invalid-customer fixtures; null for valid ones

    public Customer() {
        // required by Jackson
    }

    public Customer(String firstName, String lastName, String postalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpectedError() {
        return expectedError;
    }

    public void setExpectedError(String expectedError) {
        this.expectedError = expectedError;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        // used as the Allure/TestNG parameter label, so keep it short and readable in the report
        return fullName() + " (postCode=" + postalCode + ")";
    }
}

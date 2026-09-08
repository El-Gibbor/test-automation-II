package com.xyzbank.utils;

import com.xyzbank.models.Customer;
import net.datafaker.Faker;

/**
 * Generates fresh customer records at runtime for flows that must not collide with data left
 * behind by other test runs on the shared demo application (e.g. account creation, deletion).
 * Fixed edge-case values still live in testdata/*.json, since those need to be exact and
 * reviewable rather than random.
 */
public final class TestDataFactory {

    private static final Faker FAKER = new Faker();

    private TestDataFactory() {
    }

    /**
     * A customer with an alphabetic-only name (per acceptance criteria) and a numeric-only
     * postal code. Names are random per call and each test cleans up what it creates, so
     * collisions with fixture data (e.g. "Harry Potter") or other runs are not a concern.
     */
    public static Customer uniqueValidCustomer() {
        String firstName = alphabeticOnly(FAKER.name().firstName());
        String lastName = alphabeticOnly(FAKER.name().lastName());
        String postalCode = FAKER.number().digits(5);
        return new Customer(firstName, lastName, postalCode);
    }

    private static String alphabeticOnly(String value) {
        return value.replaceAll("[^A-Za-z]", "");
    }
}

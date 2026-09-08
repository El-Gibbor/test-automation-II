package com.xyzbank.jira;

/**
 * Jira/Xray references used by the Allure {@code @Link}/{@code @Issue} annotations on test
 * classes, kept in one place so a re-platformed Jira instance or a renamed issue is a single edit.
 */
public final class JiraLinks {

    private static final String BASE_URL = "https://amali-tech.atlassian.net/browse/";

    /** User Story 1 (Bank Manager) requirement. */
    public static final String USER_STORY_1 = BASE_URL + "TMLD3-2";
    /** User Story 2 (Customer) requirement. */
    public static final String USER_STORY_2 = BASE_URL + "TMLD3-4";
    /** Xray Test Set grouping the automated Bank Manager tests. */
    public static final String MANAGER_TEST_SET = BASE_URL + "TMLD3-79";
    /** Xray Test Set grouping the automated Customer tests. */
    public static final String CUSTOMER_TEST_SET = BASE_URL + "TMLD3-99";

    // Issue keys (not full URLs) - resolved against allure.link.issue.pattern in allure.properties.
    /** Add Customer form does not enforce the alphabetic-name / numeric-postal-code acceptance criteria. */
    public static final String DEFECT_ADD_CUSTOMER_VALIDATION = "TMLD3-88";

    private JiraLinks() {
    }
}

package com.xyzbank.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test up to twice unconditionally, including on an {@link AssertionError}.
 * Reserved for tests marked {@link FlakyEnvironment}, where an outside cause has already been
 * confirmed - see that annotation's Javadoc. Everything else uses {@link RetryAnalyzer}, which
 * deliberately does not retry an assertion failure.
 */
public class EnvironmentFlakeRetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 2;
    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts < MAX_RETRIES) {
            attempts++;
            return true;
        }
        return false;
    }
}

package com.xyzbank.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/** Retries a failed test up to twice before reporting it as failed
 *  - guards against transient UI flakiness.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

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

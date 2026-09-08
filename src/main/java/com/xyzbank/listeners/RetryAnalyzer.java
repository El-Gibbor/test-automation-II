package com.xyzbank.listeners;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test up to twice, but only when the failure looks like transient UI or
 * infrastructure flakiness: a stale element, a click intercepted by the SPA re-rendering, a
 * wait timeout, or a WebDriver-session hiccup. An {@link AssertionError}, such as an
 * acceptance-criterion mismatch against actual application behavior, is a real defect rather
 * than flakiness, so it is reported on the first failure instead of being retried and delayed.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 2;
    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts >= MAX_RETRIES || !isTransient(result.getThrowable())) {
            return false;
        }
        attempts++;
        return true;
    }

    private boolean isTransient(Throwable throwable) {
        return throwable instanceof TimeoutException
                || throwable instanceof StaleElementReferenceException
                || throwable instanceof ElementClickInterceptedException
                || throwable instanceof WebDriverException;
    }
}

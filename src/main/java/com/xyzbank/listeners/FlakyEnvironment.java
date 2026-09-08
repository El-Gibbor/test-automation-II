package com.xyzbank.listeners;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test that is known - empirically, not by assumption - to fail intermittently for a
 * reason outside the application under test and outside this suite's control, such as the
 * shared public GlobalSQA demo occasionally not recording a transaction server-side even though
 * the deposit/withdrawal itself succeeds (confirmed: balance and confirmation message are
 * correct every time; only the separate transaction-list read is sometimes missing the entry,
 * even after a 30s wait, ruling out a simple rendering race).
 *
 * <p>{@link RetryTransformer} gives a test marked with this annotation the more lenient
 * {@link EnvironmentFlakeRetryAnalyzer} (retries on any failure) instead of the default
 * {@link RetryAnalyzer} (retries only on transient WebDriver exceptions, never on an
 * {@link AssertionError}). Reach for this only after reproducing and ruling out a real defect or
 * a fixable wait condition, the way this one was - not as a default way to quiet a red test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlakyEnvironment {
}

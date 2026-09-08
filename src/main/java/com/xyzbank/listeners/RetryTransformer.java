package com.xyzbank.listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Applies a retry analyzer to every {@code @Test} automatically, so tests don't repeat that
 * boilerplate. Most tests get {@link RetryAnalyzer} (transient WebDriver failures only, never an
 * assertion mismatch). A test annotated {@link FlakyEnvironment} gets the more lenient
 * {@link EnvironmentFlakeRetryAnalyzer} instead - see that annotation's Javadoc for when that's
 * actually warranted.
 */
public class RetryTransformer implements IAnnotationTransformer {

    @Override
    @SuppressWarnings("rawtypes") // TestNG's IAnnotationTransformer interface itself declares raw types
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        boolean knownFlaky = testMethod != null && testMethod.isAnnotationPresent(FlakyEnvironment.class);
        annotation.setRetryAnalyzer(knownFlaky ? EnvironmentFlakeRetryAnalyzer.class : RetryAnalyzer.class);
    }
}

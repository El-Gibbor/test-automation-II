package com.xyzbank.listeners;

import com.xyzbank.utils.DriverFactory;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Attaches a screenshot and the page source to the Allure report automatically whenever a test
 * fails, so a failure can be diagnosed from the report alone without re-running it locally.
 */
public class AllureTestListener implements ITestListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureTestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test failed: {}", result.getName(), result.getThrowable());
        WebDriver driver = DriverFactory.getDriver();
        attachScreenshot(driver);
        attachPageSource(driver);
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    private byte[] attachScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page source on failure", type = "text/plain")
    private String attachPageSource(WebDriver driver) {
        return driver.getPageSource();
    }
}

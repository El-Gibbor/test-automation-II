package com.xyzbank.base;

import com.xyzbank.pages.HomePage;
import com.xyzbank.utils.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Common element interactions shared by every page. Locators are re-queried on each call
 * (no PageFactory caching) because the app is an AngularJS SPA that frequently re-renders
 * the DOM, which would otherwise cause StaleElementReferenceException.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.explicitWaitSeconds()));
    }

    @Step("Click element: {locator}")
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    @Step("Type '{text}' into: {locator}")
    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    /**
     * Waits briefly for a JS alert and returns its message, accepting it. Several flows on this
     * app (add customer, open account) confirm success via window.alert rather than DOM text.
     */
    @Step("Accept alert and capture its message")
    protected Optional<String> acceptAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String message = alert.getText();
            alert.accept();
            return Optional.of(message);
        } catch (TimeoutException | NoAlertPresentException e) {
            return Optional.empty();
        }
    }

    // The header (Home/Logout) is present on every screen and is an in-app (client-side routed)
    // link, unlike HomePage.open() which does a real browser navigation. The app's data lives only
    // in an Angular service for the current page load, so a hard navigation would silently reset it
    // (customers/accounts created earlier in the test would disappear) - always prefer these to move
    // between manager/customer flows within one test.
    private static final By HOME_LINK = By.cssSelector("button.home");
    private static final By LOGOUT_LINK = By.cssSelector("button.logout");

    @Step("Return to Home (in-app navigation)")
    public HomePage goHome() {
        click(HOME_LINK);
        return new HomePage(driver);
    }

    @Step("Log out")
    public HomePage logout() {
        click(LOGOUT_LINK);
        return new HomePage(driver);
    }
}

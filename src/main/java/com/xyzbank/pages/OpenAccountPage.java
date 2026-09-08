package com.xyzbank.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** "Open Account" form under the manager dashboard. Success is confirmed via a JS alert containing the new account number. */
public class OpenAccountPage extends BasePage {

    private static final By CUSTOMER_SELECT = By.id("userSelect");
    private static final By CURRENCY_SELECT = By.id("currency");
    private static final By PROCESS_BUTTON = By.cssSelector("form[name='myForm'] button[type='submit']");
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("account Number\\s*:(\\d+)");

    public OpenAccountPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open a {currency} account for customer '{customerFullName}'")
    public Optional<String> openAccount(String customerFullName, String currency) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(CUSTOMER_SELECT)))
                .selectByVisibleText(customerFullName);
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(CURRENCY_SELECT)))
                .selectByVisibleText(currency);
        click(PROCESS_BUTTON);
        return acceptAlert();
    }

    /** Extracts the account number from an "Account created successfully with account Number :1234" alert. */
    public static Optional<String> extractAccountNumber(String alertMessage) {
        Matcher matcher = ACCOUNT_NUMBER_PATTERN.matcher(alertMessage);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }
}

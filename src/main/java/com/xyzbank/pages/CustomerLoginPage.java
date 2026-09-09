package com.xyzbank.pages;

import com.xyzbank.base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/** Customer login: pick your name from a dropdown of customers the manager has added. */
public class CustomerLoginPage extends BasePage {

    private static final By CUSTOMER_SELECT = By.id("userSelect");
    private static final By LOGIN_BUTTON = By.cssSelector("form[name='myForm'] button[type='submit']");

    public CustomerLoginPage(WebDriver driver) {
        super(driver);
    }

    /** Throws NoSuchElementException if the name isn't in the dropdown - a valid failure signal in its own right. */
    @Step("Log in as customer '{customerFullName}'")
    public AccountPage loginAs(String customerFullName) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(CUSTOMER_SELECT)))
                .selectByVisibleText(customerFullName);
        click(LOGIN_BUTTON);
        return new AccountPage(driver);
    }
}

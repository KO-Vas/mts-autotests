package com.aston.mts.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CardDetailsPage {
    @FindBy(css = "[data-qa='total-amount']")
    private WebElement totalAmount;

    @FindBy(css = "[data-qa='phone-number']")
    private WebElement displayedPhone;

    public CardDetailsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getDisplayedAmount() {
        return totalAmount.getText();
    }

    public String getDisplayedPhone() {
        return displayedPhone.getText();
    }
}
package com.aston.mts.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class PaymentPage {
    @FindBy(id = "phone-input")
    private WebElement phoneInput;

    @FindBy(id = "amount-input")
    private WebElement amountInput;

    @FindBy(id = "continue-btn")
    private WebElement continueButton;

    @FindBy(css = ".payment-tab")
    private List<WebElement> paymentTabs;

    public PaymentPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void fillPhoneNumber(String phone) {
        phoneInput.clear();
        phoneInput.sendKeys(phone);
    }

    public void fillAmount(String amount) {
        amountInput.clear();
        amountInput.sendKeys(amount);
    }

    public void clickContinue() {
        continueButton.click();
    }

    public PaymentPage selectPaymentType(String type) {
        paymentTabs.stream()
                .filter(tab -> tab.getText().equalsIgnoreCase(type))
                .findFirst()
                .ifPresent(WebElement::click);
        return this;
    }
}
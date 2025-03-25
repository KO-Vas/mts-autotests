package com.aston.mts.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentPage {
    private WebDriver driver;

    @FindBy(css = "input#connection-phone")
    private WebElement phoneInput;

    @FindBy(css = "input#connection-sum")
    private WebElement amountInput;

    @FindBy(xpath = "//button[contains(., 'Продолжить')]")
    private WebElement continueButton;

    @FindBy(css = "div.payment-type-tabs button")
    private List<WebElement> paymentTabs;

    @FindBy(css = "div.payment-form input")
    private List<WebElement> formInputs;

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public PaymentPage selectPaymentType(String type) {
        paymentTabs.stream()
                .filter(tab -> tab.getText().equalsIgnoreCase(type))
                .findFirst()
                .ifPresent(WebElement::click);
        return this;
    }

    public PaymentPage enterPhone(String number) {
        phoneInput.sendKeys(number);
        return this;
    }

    public PaymentPage enterAmount(String amount) {
        amountInput.sendKeys(amount);
        return this;
    }

    public PaymentModal submit() {
        continueButton.click();
        return new PaymentModal(driver);
    }

    public List<String> getInputPlaceholders() {
        return formInputs.stream()
                .map(input -> input.getAttribute("placeholder"))
                .collect(Collectors.toList());
    }
}
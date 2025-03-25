package com.aston.mts.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentModal {
    private WebDriver driver;

    @FindBy(css = "span.phone-number")
    private WebElement phoneNumber;

    @FindBy(css = "span.amount")
    private WebElement amount;

    @FindBy(css = "div.card-form input")
    private List<WebElement> cardInputs;

    @FindBy(css = "div.payment-systems img")
    private List<WebElement> paymentIcons;

    public PaymentModal(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getPhoneNumber() {
        return phoneNumber.getText();
    }

    public String getAmount() {
        return amount.getText();
    }

    public List<String> getCardPlaceholders() {
        return cardInputs.stream()
                .map(input -> input.getAttribute("placeholder"))
                .collect(Collectors.toList());
    }

    public List<String> getPaymentSystems() {
        return paymentIcons.stream()
                .map(icon -> icon.getAttribute("alt"))
                .collect(Collectors.toList());
    }
}
package com.aston.mts.tests;

import com.aston.mts.DriverManager;
import com.aston.mts.pages.*;
import org.testng.annotations.*;
import java.util.List;
import static org.testng.Assert.*;

public class MtsPaymentTest {
    private static final String URL = "https://www.mts.by";
    private static final String PHONE = "297777777";
    private static final String AMOUNT = "5";
    private static final String[] PAYMENT_TYPES = {
            "Услуги связи",
            "Домашний интернет",
            "Рассрочка",
            "Задолженность"
    };

    @BeforeClass
    public void setup() {
        DriverManager.getDriver().get(URL);
    }

    @Test
    public void verifyPaymentForm() {
        HomePage homePage = new HomePage(DriverManager.getDriver());
        PaymentPage paymentPage = homePage.openPaymentSection();

        for (String type : PAYMENT_TYPES) {
            paymentPage.selectPaymentType(type);
            List<String> placeholders = paymentPage.getInputPlaceholders();

            switch(type) {
                case "Услуги связи":
                    assertTrue(placeholders.contains("Номер телефона"));
                    break;
                case "Домашний интернет":
                    assertTrue(placeholders.contains("Номер договора"));
                    break;
                case "Рассрочка":
                    assertTrue(placeholders.contains("Номер карты"));
                    break;
                case "Задолженность":
                    assertTrue(placeholders.contains("Номер счета"));
                    break;
            }
        }
    }

    @Test
    public void testMobilePayment() {
        PaymentPage paymentPage = new HomePage(DriverManager.getDriver())
                .openPaymentSection()
                .selectPaymentType("Услуги связи")
                .enterPhone(PHONE)
                .enterAmount(AMOUNT);

        PaymentModal modal = paymentPage.submit();

        assertEquals(modal.getPhoneNumber(), PHONE);
        assertTrue(modal.getAmount().contains(AMOUNT));

        List<String> cardFields = modal.getCardPlaceholders();
        assertTrue(cardFields.contains("Номер карты"));
        assertTrue(cardFields.contains("ММ/ГГ"));
        assertTrue(cardFields.contains("CVC/CVV"));

        List<String> paymentSystems = modal.getPaymentSystems();
        assertTrue(paymentSystems.contains("Visa"));
        assertTrue(paymentSystems.contains("Mastercard"));
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
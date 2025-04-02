package com.aston.mts.tests;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.aston.mts.pages.PaymentPage;
import com.aston.mts.pages.CardDetailsPage;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Платежи MTS")
@Feature("Онлайн пополнение")
public class MtsPaymentTest {
    private WebDriver driver;
    private PaymentPage paymentPage;
    private CardDetailsPage cardPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/path/to/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        paymentPage = new PaymentPage(driver);
        cardPage = new CardDetailsPage(driver);
    }

    @Test
    @Story("Пополнение услуг связи")
    @Description("Проверка отображения суммы и номера после ввода данных")
    @Severity(SeverityLevel.CRITICAL)
    public void testCommunicationServicesPayment() {
        Allure.step("Открываем страницу оплаты", () ->
                driver.get("https://www.mts.by/payment"));

        Allure.step("Выбираем 'Услуги связи'", () ->
                paymentPage.selectPaymentType("Услуги связи"));

        Allure.step("Заполняем номер и сумму", () -> {
            paymentPage.fillPhoneNumber("297777777");
            paymentPage.fillAmount("10");
            paymentPage.clickContinue();
        });

        Allure.step("Проверяем данные", () -> {
            assertThat(cardPage.getDisplayedAmount()).isEqualTo("10 BYN");
            assertThat(cardPage.getDisplayedPhone()).isEqualTo("297777777");
        });
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
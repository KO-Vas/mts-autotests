package com.aston.mts;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import static org.testng.Assert.*;

public class MtsTest {
    private static final String BASE_URL = "https://www.mts.by";
    private static final String PHONE_NUMBER = "297777777";
    private static final String PAYMENT_AMOUNT = "5";

    @BeforeClass
    public void setUp() {
        DriverManager.getDriver().get(BASE_URL);
        acceptCookies();
    }

    private void acceptCookies() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
            WebElement cookieButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//button[contains(., 'Принять')]")
                    )
            );
            cookieButton.click();
        } catch (TimeoutException ignored) {}
    }

    @Test(priority = 1)
    public void verifyBlockTitle() {
        WebElement title = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[normalize-space()='Онлайн пополнение без комиссии']")
                ));
        assertTrue(title.isDisplayed());
    }

    @Test(priority = 2)
    public void verifyVisaLogo() {
        WebElement visaLogo = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//img[@alt='Visa']")
                ));
        assertTrue(visaLogo.isDisplayed());
    }

    @Test(priority = 3)
    public void verifyDetailsLink() {
        WebElement link = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(), 'Подробнее о сервисе')]")
                ));
        link.click();
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.urlContains("poryadok-oplaty"));
    }

    @Test(priority = 4)
    public void testContinueButton() {
        DriverManager.getDriver().get(BASE_URL);
        acceptCookies();

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(25));

        WebElement phoneInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input#connection-phone")
                )
        );
        phoneInput.sendKeys(PHONE_NUMBER);

        WebElement amountInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("input#connection-sum.total_rub")
                )
        );
        amountInput.sendKeys(PAYMENT_AMOUNT);

        WebElement continueButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(., 'Продолжить')]")
                )
        );
        continueButton.click();

        try {
            WebElement paymentModal = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.payment-modal h3.payment-modal__title")
                    )
            );
            assertTrue(paymentModal.getText().contains("Оплата"));

            WebElement displayedNumber = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.payment-modal span.phone-number")
                    )
            );
            assertEquals(displayedNumber.getText(), PHONE_NUMBER);

        } catch (TimeoutException e) {
            File screenshot = ((TakesScreenshot)DriverManager.getDriver())
                    .getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("target/screenshots/error_screenshot.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            fail("Модальное окно оплаты не появилось");
        }
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
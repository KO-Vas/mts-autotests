package com.aston.mts.tests;

import com.aston.mts.DriverManager;
import org.openqa.selenium.logging.LogType;

import java.util.logging.Level;
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
            WebElement cookieButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., 'Принять')]")));
            cookieButton.click();
        } catch (TimeoutException ignored) {}
    }

    @Test(priority = 1)
    public void verifyBlockTitle() {
        WebElement title = DriverManager.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[normalize-space()='Онлайн пополнение без комиссии']")));
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
        WebElement link = DriverManager.getWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Подробнее о сервисе')]")));
        link.click();
        DriverManager.getWait().until(ExpectedConditions.urlContains("poryadok-oplaty"));
    }

    @Test(priority = 4)
    public void testContinueButton() {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
                        driver.get(BASE_URL);

                        acceptCookies();

                        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

                        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#connection-phone")
            ));
            phoneInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, PHONE_NUMBER);

                        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("input#connection-sum")
            ));
            amountInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE, PAYMENT_AMOUNT);

                        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'Продолжить')]")
            ));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'}); arguments[0].click();",
                    continueButton
            );

                        WebElement paymentForm = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//form[contains(@class, 'payment-form')]")
            ));


            assertTrue(paymentForm.isDisplayed(), "Форма оплаты не отображается");

        } catch (TimeoutException e) {
                driver.manage().logs().get(LogType.BROWSER).forEach(logEntry -> {
                if (logEntry.getLevel() == Level.SEVERE) {
                    System.err.println("Браузерная ошибка: " + logEntry.getMessage());
                }
            });

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(screenshot, new File("error_screenshot.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            fail("Тест не пройден: " + e.getMessage());
        }
    }

    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
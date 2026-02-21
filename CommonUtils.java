package utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    
    private static final String SCREENSHOT_BASE_PATH = "test-output/screenshots/";
    
    /**
     * Capture screenshot organized by test class, test case ID, test name, and PASS/FAIL
     */
    @Step("Capture screenshot for {testCaseId} - {testName}")
    public static String captureScreenshot(WebDriver driver, String status, 
                                          String testClassName, String testCaseId, 
                                          String testName) {
        
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        
        // Folder structure: screenshots/TestClassName/TestCaseId_TestName/STATUS/
        String folderPath = String.format("%s%s/%s_%s/%s/", 
            SCREENSHOT_BASE_PATH, 
            testClassName,
            testCaseId, 
            testName,
            status);
        
        // Filename: TestCaseId_TestName_timestamp.png
        String fileName = String.format("%s_%s_%s.png", testCaseId, testName, timestamp);
        String fullPath = folderPath + fileName;
        
        try {
            // Create directories if they don't exist
            Files.createDirectories(Paths.get(folderPath));
            
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(screenshot, new File(fullPath));
            
            System.out.println("📸 Screenshot saved: " + fullPath);
            
            // Attach to Allure with test case ID in name
            attachScreenshotToAllure(driver, testCaseId + " - " + testName + " - " + status);
            
            return fullPath;
            
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }
    
    @Attachment(value = "{0}", type = "image/png")
    public static byte[] attachScreenshotToAllure(WebDriver driver, String attachmentName) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Attachment(value = "Page Source - {0} - {1}", type = "text/html")
    public static String attachPageSource(WebDriver driver, String testCaseId, String testName) {
        return driver.getPageSource();
    }
    
    @Step("Accept cookies")
    public static void acceptCookies(WebDriver driver) {
        try {
            WebElement acceptCookies = driver.findElement(By.id("accept-cookies"));
            if (acceptCookies.isDisplayed()) {
                acceptCookies.click();
                System.out.println("🍪 Cookies accepted");
            }
        } catch (Exception e) {
            // Cookie popup not present
        }
    }
    
    @Step("Handle popup")
    public static void handlePopup(WebDriver driver) {
        try {
            WebElement closePopup = driver.findElement(By.className("close-popup"));
            if (closePopup.isDisplayed()) {
                closePopup.click();
                System.out.println("🪟 Popup closed");
            }
        } catch (Exception e) {
            // Popup not present
        }
    }
    
    @Step("Log message")
    public static void logToAllure(String message) {
        System.out.println("📝 " + message);
    }
}
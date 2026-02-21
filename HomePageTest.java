package tests;

import base.BaseTest;
import pages.HomePage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import annotations.TestCase;
import utils.CommonUtils;

import java.util.List;

import org.openqa.selenium.WebElement;

@Epic("E-commerce Website Testing")
@Feature("Home Page")
public class HomePageTest extends BaseTest {
    
    private HomePage homePage;
    
    @BeforeMethod
    public void initializePages() {
        homePage = new HomePage(getDriver());
    }
    
    // ==================== TEST CASE TCHP01: HOME PAGE LOAD ====================
    @Test(priority = 1, description = "Verify home page loads successfully")
    @TestCase(id = "TCHP01", description = "Home page load verification")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify that home page loads with correct title")
    @Story("Home Page Loading")
    public void testHomePageLoad() {
        String testCaseId = "TCHP01";
        String testName = "testHomePageLoad";
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 TCHP01: Home Page Load Test");
        System.out.println("📋 =========================================\n");
        
        String pageTitle = homePage.getPageTitle();
        Allure.addAttachment("Actual Page Title", pageTitle);
        
        Assert.assertTrue(pageTitle.contains("Expected"), 
            "Home page title does not match! Found: " + pageTitle);
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ TCHP01 PASSED: Home page loaded successfully\n");
    }
    
    // ==================== TEST CASE TCHP15: MAIN MENU NAVIGATION ====================
    @Test(priority = 2, description = "Test all main menu navigations")
    @TestCase(id = "TCHP15", description = "Main menu navigation test")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Navigate through all main menus and verify each loads")
    @Story("Main Menu Navigation")
    public void testMainMenuNavigation() throws Exception {
        String testCaseId = "TCHP15";
        String testName = "testMainMenuNavigation";
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 TCHP15: Main Menu Navigation Test");
        System.out.println("📋 =========================================\n");
        
        // Initialize page
        homePage.initializePage();
        
        // Navigate all menus - pass testCaseId
        homePage.navigateAllMenus(testCaseId);
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ TCHP15 PASSED: All main menus navigated successfully\n");
    }
    
    // ==================== TEST CASE TCHP02: MENU COUNT VERIFICATION ====================
    @Test(priority = 5, description = "Verify main menu count")
    @TestCase(id = "TCHP02", description = "Menu count verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all expected main menus are present")
    @Story("Menu Structure Validation")
    public void testMainMenuCount() {
        String testCaseId = "TCHP02";
        String testName = "testMainMenuCount";
        int expectedMinimumMenus = 5;
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 TCHP02: Menu Count Verification");
        System.out.println("📋 =========================================\n");
        
        // This method exists in HomePage.java
        int actualMenuCount = homePage.getMainMenuCount();
        System.out.println("Found " + actualMenuCount + " main menus");
        
        Allure.addAttachment("Actual Menu Count", String.valueOf(actualMenuCount));
        Allure.addAttachment("Expected Minimum", String.valueOf(expectedMinimumMenus));
        
        Assert.assertTrue(actualMenuCount >= expectedMinimumMenus, 
            "Menu count too low! Found: " + actualMenuCount + ", Expected minimum: " + expectedMinimumMenus);
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ TCHP02 PASSED: Menu count verification successful\n");
    }
}
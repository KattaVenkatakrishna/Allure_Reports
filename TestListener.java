package utils;

import org.testng.ITestListener;
import org.testng.ITestResult;

import annotations.TestCase;

import org.testng.ITestContext;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Attachment;
import base.BaseTest;

import java.lang.reflect.Method;

public class TestListener implements ITestListener {
    
    @Attachment(value = "Error Log - {0} - {1}", type = "text/plain")
    public static String attachLogs(String testCaseId, String testName, String log) {
        return log;
    }
    
    private String getTestClassName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName();
    }
    
    private String getTestName(ITestResult result) {
        return result.getMethod().getMethodName();
    }
    
    /**
     * Extract Test Case ID from method or annotation
     * You can customize this based on how you store test case IDs
     */
    private String getTestCaseId(ITestResult result) {
        // Option 1: Get from method annotation
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        if (method.isAnnotationPresent(TestCase.class)) {
        	TestCase testCase = method.getAnnotation(TestCase.class);
            return testCase.id();
        }
        
        // Option 2: Extract from test name pattern (e.g., testMenuNavigation_TCHP15)
        String testName = getTestName(result);
        if (testName.contains("_")) {
            String[] parts = testName.split("_");
            if (parts.length > 1 && parts[1].startsWith("TC")) {
                return parts[1];
            }
        }
        
        // Option 3: Return default or extract from description
        return "TC000"; // Default test case ID
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        String testClass = getTestClassName(result);
        String testName = getTestName(result);
        String testCaseId = getTestCaseId(result);
        
        System.out.println("\n🔵 =========================================");
        System.out.println("🔵 TEST STARTED: " + testClass + " - " + testCaseId + " - " + testName);
        System.out.println("🔵 =========================================\n");
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        String testClass = getTestClassName(result);
        String testName = getTestName(result);
        String testCaseId = getTestCaseId(result);
        
        System.out.println("\n✅ =========================================");
        System.out.println("✅ TEST PASSED: " + testClass + " - " + testCaseId + " - " + testName);
        System.out.println("✅ =========================================\n");
        
        // Take success screenshot with test case ID
        Object testClassObj = result.getInstance();
        if (testClassObj instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testClassObj).getDriver();
            if (driver != null) {
                CommonUtils.captureScreenshot(
                    driver, "PASS", testClass, testCaseId, testName
                );
            }
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        String testClass = getTestClassName(result);
        String testName = getTestName(result);
        String testCaseId = getTestCaseId(result);
        
        System.out.println("\n❌ =========================================");
        System.out.println("❌ TEST FAILED: " + testClass + " - " + testCaseId + " - " + testName);
        System.out.println("❌ Error: " + result.getThrowable().getMessage());
        System.out.println("❌ =========================================\n");
        
        Object testClassObj = result.getInstance();
        if (testClassObj instanceof BaseTest) {
            WebDriver driver = ((BaseTest) testClassObj).getDriver();
            
            if (driver != null) {
                // 1. Take failure screenshot with test case ID
                CommonUtils.captureScreenshot(
                    driver, "FAIL", testClass, testCaseId, testName
                );
                
                // 2. Attach page source with test case ID
                CommonUtils.attachPageSource(driver, testCaseId, testName);
                
                // 3. Attach error log with test case ID
                if (result.getThrowable() != null) {
                    attachLogs(testCaseId, testName, 
                        result.getThrowable().toString());
                }
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testClass = getTestClassName(result);
        String testName = getTestName(result);
        String testCaseId = getTestCaseId(result);
        System.out.println("\n⚠️ TEST SKIPPED: " + testClass + " - " + testCaseId + " - " + testName + "\n");
    }
}
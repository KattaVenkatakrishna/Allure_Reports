package tests;

import base.BaseTest;
import pages.CheckoutPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import annotations.TestCase;
import utils.CommonUtils;

@Epic("E-commerce Website Testing")
@Feature("Checkout Page")
public class CheckoutPageTest extends BaseTest {
    
    private CheckoutPage checkoutPage;
    
    // Test Data
    private final String VALID_PINCODE = "500016";
    private final String FULL_NAME = "Sasikanth";
    private final String ADDRESS = "Hyderabad";
    private final String MOBILE = "9000000000";
    private final String INVALID_NAME = "<>@@#@#<>";
    private final String INVALID_MOBILE = "9878";
    private final String INVALID_PINCODE = "512";
    private final String MENU_CATEGORY = "Watches"; // Update with your actual menu
    private final String PRODUCT_NAME = "Fossil Gen 6"; // Update with your actual product
    
    @BeforeMethod
    public void initializePages() {
        checkoutPage = new CheckoutPage(getDriver());
    }
    
    @Test(priority = 1, description = "Navigate to list page and add multiple products to cart")
    @TestCase(id = "CHK01", description = "List navigation and add to cart")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Navigate to product listing page and add multiple items to cart")
    @Story("Shopping Cart")
    public void testAddMultipleProductsToCart() throws Exception {
        String testCaseId = "CHK01";
        String testName = "testAddMultipleProductsToCart";
        int productsToAdd = 3;
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 CHK01: List Navigation & Add Multiple Products");
        System.out.println("📋 =========================================\n");
        
        // Step 1: Navigate to list page
        Allure.step("Navigate to " + MENU_CATEGORY + " list page");
        checkoutPage.navigateToListPage(MENU_CATEGORY);
        Thread.sleep(3000);
        
        // Step 2: Get product count
        int totalProducts = checkoutPage.getProductCount();
        System.out.println("Found " + totalProducts + " products on list page");
        Allure.addAttachment("Total Products Found", String.valueOf(totalProducts));
        
        // Step 3: Add multiple products to cart
        for (int i = 0; i < productsToAdd && i < totalProducts; i++) {
            Allure.step("Adding product " + (i + 1) + " to cart");
            
            // Select product
            checkoutPage.selectProductByIndex(i);
            Thread.sleep(3000);
            
            // Add to bag
            checkoutPage.addToBag();
            Thread.sleep(2000);
            
            // Take screenshot after adding
            CommonUtils.captureScreenshot(getDriver(), "INFO", 
                this.getClass().getSimpleName(), testCaseId, 
                testName + "_Product_" + (i + 1));
            
            // Continue shopping (not checkout yet)
            if (i < productsToAdd - 1) {
                checkoutPage.continueShopping();
                Thread.sleep(3000);
                // Navigate back to list page if needed
                checkoutPage.navigateToListPage(MENU_CATEGORY);
                Thread.sleep(2000);
            }
        }
        
        // Step 4: Go to cart
        Allure.step("Navigate to cart");
        checkoutPage.goToCart();
        Thread.sleep(3000);
        
        // Step 5: Verify cart has items
        int cartItemCount = checkoutPage.getCartItemCount();
        System.out.println("Cart has " + cartItemCount + " items");
        Allure.addAttachment("Cart Item Count", String.valueOf(cartItemCount));
        
        Assert.assertTrue(cartItemCount > 0, "Cart is empty!");
        Assert.assertEquals(cartItemCount, productsToAdd, 
            "Expected " + productsToAdd + " items but found " + cartItemCount);
        
        // Step 6: Proceed to checkout
        Allure.step("Proceed to checkout");
        checkoutPage.proceedToCheckout();
        Thread.sleep(3000);
        
        // Step 7: Verify checkout page
        boolean isDisplayed = checkoutPage.isOrderSummaryDisplayed();
        Assert.assertTrue(isDisplayed, "Order Summary not displayed");
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ CHK01 PASSED: Added " + productsToAdd + " products to cart\n");
    }
    
    @Test(priority = 2, description = "Select specific product by name")
    @TestCase(id = "CHK02", description = "Select product by name")
    @Severity(SeverityLevel.NORMAL)
    @Description("Search and select a specific product by name")
    @Story("Product Selection")
    public void testSelectSpecificProduct() throws Exception {
        String testCaseId = "CHK02";
        String testName = "testSelectSpecificProduct";
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 CHK02: Select Specific Product");
        System.out.println("📋 =========================================\n");
        
        // Navigate to list page
        checkoutPage.navigateToListPage(MENU_CATEGORY);
        Thread.sleep(3000);
        
        // Select specific product
        Allure.step("Select product: " + PRODUCT_NAME);
        checkoutPage.selectProductByName(PRODUCT_NAME);
        Thread.sleep(3000);
        
        // Add to cart
        checkoutPage.addToBag();
        Thread.sleep(2000);
        
        // Verify cart
        checkoutPage.goToCart();
        Thread.sleep(3000);
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ CHK02 PASSED: Selected product: " + PRODUCT_NAME + "\n");
    }
    
    @Test(priority = 3, description = "Validate shipping address name field")
    @TestCase(id = "CHK03", description = "Shipping address name validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify validation for special characters in name field")
    @Story("Address Validation")
    public void testShippingAddressNameValidation() throws Exception {
        String testCaseId = "CHK03";
        String testName = "testShippingAddressNameValidation";
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 CHK03: Shipping Address Name Validation");
        System.out.println("📋 =========================================\n");
        
        // Click add new address
        checkoutPage.clickAddNewAddress();
        Thread.sleep(3000);
        
        // Enter invalid name
        checkoutPage.enterFullName(INVALID_NAME);
        Thread.sleep(2000);
        
        // Scroll and click save
        checkoutPage.scrollToElement(checkoutPage.mobileInput);
        Thread.sleep(2000);
        checkoutPage.clickSaveAddress();
        Thread.sleep(3000);
        
        // Verify error message
        checkoutPage.scrollUp();
        Thread.sleep(3000);
        
        String actualMessage = checkoutPage.getInvalidNameMessage();
        String expectedMessage = "Receiver's Name field cannot include special characters.";
        
        Assert.assertEquals(actualMessage, expectedMessage);
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ CHK03 PASSED: Name validation working\n");
    }
    
    @Test(priority = 4, description = "Add shipping address and continue to payment")
    @TestCase(id = "CHK04", description = "Complete address and continue to payment")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Fill all address fields and proceed to payment")
    @Story("Checkout Flow")
    public void testAddShippingAddressAndContinue() throws Exception {
        String testCaseId = "CHK04";
        String testName = "testAddShippingAddressAndContinue";
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 CHK04: Add Address and Continue to Payment");
        System.out.println("📋 =========================================\n");
        
        // Enter valid details
        checkoutPage.enterFullName(FULL_NAME);
        Thread.sleep(2000);
        
        checkoutPage.enterAddress(ADDRESS);
        Thread.sleep(2000);
        
        checkoutPage.enterMobile(MOBILE);
        Thread.sleep(2000);
        
        checkoutPage.enterPincode(VALID_PINCODE);
        Thread.sleep(3000);
        
        checkoutPage.scrollToElement(checkoutPage.mobileInput);
        Thread.sleep(3000);
        
        checkoutPage.scrollUp();
        Thread.sleep(2000);
        
        checkoutPage.selectState();
        Thread.sleep(3000);
        
        checkoutPage.clickSaveAddress();
        Thread.sleep(3000);
        
        // Verify continue to payment button
        String actualText = checkoutPage.getContinueToPaymentText();
        String expectedText = "Continue To Payment";
        
        Assert.assertEquals(actualText, expectedText);
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ CHK04 PASSED: Address added and payment page reached\n");
    }
    
    @Test(priority = 5, description = "Complete end-to-end checkout flow")
    @TestCase(id = "CHK05", description = "Complete checkout flow")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Complete full checkout process from list page to payment")
    @Story("End-to-End Flow")
    public void testCompleteCheckoutFlow() throws Exception {
        String testCaseId = "CHK05";
        String testName = "testCompleteCheckoutFlow";
        
        Allure.addAttachment("Test Case ID", testCaseId);
        System.out.println("\n📋 =========================================");
        System.out.println("📋 CHK05: Complete Checkout Flow");
        System.out.println("📋 =========================================\n");
        
        // STEP 1: Navigate to list page
        Allure.step("STEP 1: Navigate to product listing");
        checkoutPage.navigateToListPage(MENU_CATEGORY);
        Thread.sleep(3000);
        
        // STEP 2: Add product to cart
        Allure.step("STEP 2: Add product to cart");
        checkoutPage.selectProductByIndex(0);
        Thread.sleep(3000);
        checkoutPage.addToBag();
        Thread.sleep(2000);
        
        // STEP 3: Go to cart and checkout
        Allure.step("STEP 3: Go to cart");
        checkoutPage.goToCart();
        Thread.sleep(3000);
        
        Allure.step("STEP 4: Proceed to checkout");
        checkoutPage.proceedToCheckout();
        Thread.sleep(3000);
        
        // STEP 5: Add shipping address
        Allure.step("STEP 5: Add shipping address");
        checkoutPage.clickAddNewAddress();
        Thread.sleep(2000);
        
        checkoutPage.enterFullName(FULL_NAME);
        checkoutPage.enterMobile(MOBILE);
        checkoutPage.enterAddress(ADDRESS);
        checkoutPage.enterPincode(VALID_PINCODE);
        
        checkoutPage.scrollToElement(checkoutPage.mobileInput);
        Thread.sleep(2000);
        checkoutPage.selectState();
        Thread.sleep(2000);
        checkoutPage.clickSaveAddress();
        Thread.sleep(3000);
        
        // STEP 6: Verify payment page
        Allure.step("STEP 6: Verify payment page");
        String paymentText = checkoutPage.getContinueToPaymentText();
        Assert.assertEquals(paymentText, "Continue To Payment");
        
        CommonUtils.captureScreenshot(getDriver(), "PASS", 
            this.getClass().getSimpleName(), testCaseId, testName);
        
        System.out.println("\n✅ CHK05 PASSED: Complete checkout flow successful\n");
    }
}
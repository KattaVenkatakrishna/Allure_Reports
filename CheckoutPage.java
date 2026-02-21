package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.List;

public class CheckoutPage {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    JavascriptExecutor js;
    
    // ==================== LIST PAGE ELEMENTS ====================
    
    @FindBy(xpath = "//ul[@class='nav navbar-nav']/li[1]") // Update with your actual menu locator
    public WebElement mainMenu;
    
    @FindBy(xpath = "//div[contains(@class,'product-tile') or contains(@class,'list-img')]")
    public List<WebElement> productItems;
    
    @FindBy(xpath = "//div[contains(@class,'pdp-link')]")
    public List<WebElement> productLinks;
    
    @FindBy(xpath = "//div[contains(@class,'pagination')]//a[text()='2']")
    public WebElement nextPageButton;
    
    // ==================== PRODUCT SELECTION ELEMENTS ====================
    
    @FindBy(xpath = "(//div[contains(@class,'pdp-link')]/a)[1]")
    public WebElement firstProduct;
    
    @FindBy(xpath = "//*[contains(text(),'Add To Bag') or contains(text(), 'Add to Bag')]")
    public WebElement addToBagButton;
    
    @FindBy(xpath = "//*[@title ='View Cart']//span[@id='cartCount' or @class = 'minicart-quantity']")
    public WebElement cartIcon;
    
    @FindBy(xpath = "//button[contains(@class,'continue-shopping')]")
    public WebElement continueShoppingButton;
    
    @FindBy(xpath = "//div[@class='mt-3' or contains(@class,'checkout-continue')]//*[text()='checkout' or contains(text(), 'Checkout') or @id='btn-chkout']")
    public WebElement checkoutButton;
    
    @FindBy(xpath = "//*[text()='Order Summary' or text() = 'order summary' or text() = 'ORDER SUMMARY' or @class = 'order-summary-title']")
    public WebElement orderSummary;
    
    @FindBy(xpath = "//*[contains(@class,'cart-item') or @class = 'card d-flex' or contains(@class, 'card product-info')]")
    public List<WebElement> cartItems;
    
    @FindBy(xpath = "//div[contains(@class,'cart-total')]")
    public WebElement cartTotal;
    
    // ==================== ADDRESS FORM ELEMENTS ====================
    
    @FindBy(xpath = "//*[text()=' Add new address ']")
    public WebElement addNewAddressButton;
    
    @FindBy(xpath = "//*[@id='shipping-first-name']")
    public WebElement fullNameInput;
    
    @FindBy(xpath = "//*[@id='shipping-phone-number']")
    public WebElement mobileInput;
    
    @FindBy(xpath = "(//*[@id='shipping-address'])[2]")
    public WebElement addressInput;
    
    @FindBy(xpath = "//*[@id='shipping-zip-code']")
    public WebElement pincodeInput;
    
    @FindBy(xpath = "//*[@id='shipping-state']")
    public WebElement stateDropdown;
    
    @FindBy(xpath = "//*[text()=' Save & use this address ']")
    public WebElement saveAddressButton;
    
    @FindBy(xpath = "//*[text()=' Continue to Payment ']")
    public WebElement continueToPaymentButton;
    
    // ==================== VALIDATION MESSAGE ELEMENTS ====================
    
    @FindBy(xpath = "//*[@class='is-invalid']/span")
    public WebElement invalidNameMessage;
    
    @FindBy(xpath = "(//*[text()=' Please enter a valid 10 digit mobile number.'])")
    public WebElement invalidMobileMessage;
    
    @FindBy(xpath = "(//*[@class='is-invalid']/span)[2]")
    public WebElement pincodeErrorMessage;
    
    @FindBy(xpath = "(//*[text()='Please enter a valid pincode.'])")
    public WebElement invalidPincodeMessage;
    
    // ==================== CONSTRUCTOR ====================
    
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }
    
    // ==================== LIST PAGE NAVIGATION METHODS ====================
    
    public void navigateToListPage(String menuName) {
        // Click on main menu to go to list page   
        WebElement menu = driver.findElement(By.xpath("//li[contains(@id,'menu')]/div/a[contains(text(), '"+menuName+"')]"));
        actions.moveToElement(menu).click().perform();
        System.out.println("📋 Navigated to " + menuName + " list page");
    }
    
    public int getProductCount() {
        return productItems.size();
    }
    
    public void selectProductByIndex(int index) {
        if (index < productItems.size()) {
            productItems.get(index).click();
            System.out.println("📦 Selected product at index: " + index);
        }
    }
    
    public void selectProductByName(String productName) {
        for (WebElement product : productLinks) {
            if (product.getText().contains(productName)) {
                product.click();
                System.out.println("📦 Selected product: " + productName);
                break;
            }
        }
    }
    
    public void goToNextPage() {
        nextPageButton.click();
        System.out.println("📄 Navigated to next page");
    }
    
    // ==================== CART MANAGEMENT METHODS ====================
    
    public void selectFirstProduct() {
        actions.moveToElement(firstProduct).click().perform();
    }
    
    public void addToBag() {
        addToBagButton.click();
        System.out.println("🛒 Added to bag");
    }
    
    public void continueShopping() {
        continueShoppingButton.click();
        System.out.println("🛍️ Continuing shopping");
    }
    
    public void goToCart() {
        cartIcon.click();
        System.out.println("🛒 Navigated to cart");
    }
    
    public void proceedToCheckout() {
        actions.moveToElement(checkoutButton).click().perform();
        System.out.println("💳 Proceeding to checkout");
    }
    
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    public String getCartTotal() {
        return cartTotal.getText();
    }
    
    // ==================== ADDRESS MANAGEMENT METHODS ====================
    
    public void clickAddNewAddress() {
        addNewAddressButton.click();
        System.out.println("➕ Adding new address");
    }
    
    public void enterFullName(String name) {
        fullNameInput.clear();
        fullNameInput.sendKeys(name);
        System.out.println("📝 Entered name: " + name);
    }
    
    public void enterMobile(String mobile) {
        mobileInput.clear();
        mobileInput.sendKeys(mobile);
        System.out.println("📱 Entered mobile: " + mobile);
    }
    
    public void enterAddress(String address) {
        addressInput.clear();
        addressInput.sendKeys(address);
        System.out.println("🏠 Entered address: " + address);
    }
    
    public void enterPincode(String pincode) {
        pincodeInput.clear();
        pincodeInput.sendKeys(pincode);
        System.out.println("📍 Entered pincode: " + pincode);
    }
    
    public void selectState() {
        stateDropdown.click();
        System.out.println("🔽 Selected state");
    }
    
    public void clickSaveAddress() {
        js.executeScript("arguments[0].click();", saveAddressButton);
        System.out.println("💾 Saved address");
    }
    
    // ==================== UTILITY METHODS ====================
    
    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
    
    public void scrollUp() {
        js.executeScript("window.scrollBy(0,-250);");
    }
    
    public void scrollDown() {
        js.executeScript("window.scrollBy(0,250);");
    }
    
    // ==================== VERIFICATION METHODS ====================
    
    public boolean isOrderSummaryDisplayed() {
        return orderSummary.isDisplayed();
    }
    
    public String getInvalidNameMessage() {
        return invalidNameMessage.getText();
    }
    
    public String getInvalidMobileMessage() {
        return invalidMobileMessage.getText();
    }
    
    public String getInvalidPincodeMessage() {
        return invalidPincodeMessage.getText();
    }
    
    public String getPincodeErrorMessage() {
        return pincodeErrorMessage.getText();
    }
    
    public String getContinueToPaymentText() {
        return continueToPaymentButton.getText();
    }
}
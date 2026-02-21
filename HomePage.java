package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {
    WebDriver driver;
    JavascriptExecutor js;
    Actions actions;
    WebDriverWait wait;
    
    // Locators
    @FindBy(xpath = "//img[@class='icon-logo w-auto']")
    WebElement logoIcon;
    
    @FindBy(xpath = "//ul[@class='nav navbar-nav']/li[5]")
    WebElement brandMenuLink;
    
    @FindBy(xpath = "//div[@class='megamenu-column newMegamenuLogo mb-0']/div")
    List<WebElement> brandColumns;
    
    @FindBy(xpath = "//img[@type='image/jpg']")
    WebElement pageLoadImage;
    
    @FindBy(xpath = "//title")
    WebElement pageTitle;
    
    // Dynamic locators
    private By mainMenuLocator = By.xpath("//li[contains(@id,'menu')]");
    private By subMenuLocator = By.xpath("//h4[contains(@class,'mblblk-active')]/a");
    private By subMenuLinksLocator = By.xpath("//ul[contains(@class,'megamenulinks-ul')]//li/a");
    private By brandMenuLocator = By.xpath("//ul[@class='nav navbar-nav']/li[5]");
    private By brandColumnsLocator = By.xpath("//div[@class='megamenu-column newMegamenuLogo mb-0']/div");
    private By brandImagesLocator = By.xpath("//div[@class='megamenu-column newMegamenuLogo mb-0']/div/a/img");
    private By homeTemplateImagesLocator = By.xpath("(//div[@id='homeTemplate']//img)");
    private By footerLinksLocator = By.xpath("(//div[@class='container px-lg-0 px-lg-3 border-top mt-4']//a)");  //discuss this
    private By salesTemplateImagesLocator = By.xpath("//div[@id='salesTemplate']//img"); //discuss this
    private By pageLoadImageLocator = By.xpath("//img[@type='image/jpg']"); //disc this
    
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.actions = new Actions(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Creates FluentWait instance
     */
    private FluentWait<WebDriver> createFluentWait() {
        return new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(45))
            .pollingEvery(Duration.ofSeconds(2))
            .ignoring(NoSuchElementException.class)
            .ignoring(StaleElementReferenceException.class);
    }
    
    // ==================== INITIALIZATION METHODS ====================
    
    @Step("Initialize page - handle cookies and popups")
    public void initializePage() throws InterruptedException {
        Thread.sleep(2000);
        // Add your cookie acceptance logic here
        // CommonUtils.acceptCookies(driver);
        Thread.sleep(1000);
        // Add your popup handling logic here
        // CommonUtils.handlePopup(driver);
    }
    
    // ==================== MENU NAVIGATION METHODS ====================
    
    @Step("Navigate through all menus and submenus")
    public void navigateAllMenus(String testCaseId) throws InterruptedException {
        FluentWait<WebDriver> fluentWait = createFluentWait();
        
        // Wait for main menu
        List<WebElement> menuLinks = fluentWait.until(
            ExpectedConditions.presenceOfAllElementsLocatedBy(mainMenuLocator)
        );
        
        System.out.println("📋 Total main menus found: " + menuLinks.size());
        
        for (int i = 0; i < menuLinks.size(); i++) {
            // Refresh menu list
            menuLinks = driver.findElements(mainMenuLocator);
            WebElement mainMenu = menuLinks.get(i);
            
            String menuText = mainMenu.getText().trim();
            if (menuText.isEmpty()) continue;
            
            // Scroll and hover
            scrollToElement(mainMenu);
            System.out.println("📌 Processing Main Menu: " + menuText);
            hoverOverMenu(mainMenu);
            
            // Wait for submenu
            List<WebElement> subMenus = fluentWait.until(driver -> {
                List<WebElement> els = driver.findElements(subMenuLocator);
                return els.size() > 0 ? els : null;
            });
            
            System.out.println("  📌 Submenus under '" + menuText + "': " + subMenus.size());
            
            // Process submenus
            for (int j = 0; j < subMenus.size(); j++) {
                // Re-hover main menu
                hoverOverMenu(mainMenu);
                
                // Wait and refresh submenu list
                fluentWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(subMenuLinksLocator));
                subMenus = driver.findElements(subMenuLinksLocator);
                
                WebElement subMenu = subMenus.get(j);
                String subMenuText = subMenu.getText().trim();
                
                if (subMenuText.isEmpty()) continue;
                
                System.out.println("    → Clicking submenu: " + subMenuText);
                
                // Click submenu
                clickSubMenu(subMenu);
                
                // Verify page load
                try {
                    fluentWait.until(ExpectedConditions.presenceOfElementLocated(pageLoadImageLocator));
                    System.out.println("    ✅ Page loaded successfully");
                } catch (TimeoutException e) {
                    System.out.println("    ❌ Page failed to load");
                }
                
                // Navigate back
                driver.navigate().back();
                Thread.sleep(1000);
                
                // Re-hover main menu
                hoverOverMenu(mainMenu);
            }
        }
    }
    
    @Step("Get main menu count")
    public int getMainMenuCount() {
        return driver.findElements(mainMenuLocator).size();
    }
    
    // ==================== BRANDS NAVIGATION METHODS ====================
    
    @Step("Navigate back to previous page")
    public void navigateBack() {
        driver.navigate().back();
        System.out.println("🔙 Navigated back");
    }
    
    @Step("Click on logo icon to return to homepage")
    public void clickLogoIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(logoIcon));
        logoIcon.click();
        System.out.println("🏠 Clicked on logo icon");
    }
    
    @Step("Hover over Brand menu")
    public void hoverOverBrandMenu() {
        wait.until(ExpectedConditions.presenceOfElementLocated(brandMenuLocator));
        WebElement brandLink = driver.findElement(brandMenuLocator);
        actions.moveToElement(brandLink).perform();
        System.out.println("🖱️ Hovered over Brand menu");
    }
    
    @Step("Get all brand columns")
    public List<WebElement> getBrandColumns() {
        return driver.findElements(brandColumnsLocator);
    }
    
    @Step("Get brand images from column {columnIndex}")
    public List<WebElement> getBrandImagesFromColumn(int columnIndex) {
        String xpath = "//div[@class='megamenu-column newMegamenuLogo mb-0']/div[" + columnIndex + "]/a/img";
        return driver.findElements(By.xpath(xpath));
    }
    
    @Step("Click on brand image: {brandName}")
    public void clickBrandImage(WebElement brandImage, String brandName) {
        brandImage.click();
        System.out.println("🖱️ Clicked on brand: " + brandName);
    }
    
    @Step("Get all product images from sales template page")
    public List<WebElement> getSalesTemplateImages() {
        wait.until(ExpectedConditions.presenceOfElementLocated(salesTemplateImagesLocator));
        return driver.findElements(salesTemplateImagesLocator);
    }
    
    @Step("Click on product image: {imageName}")
    public void clickProductImage(WebElement image, String imageName) {
        actions.moveToElement(image).perform();
        image.click();
        System.out.println("🖱️ Clicked on product image: " + imageName);
    }
    
    // ==================== HOMEPAGE LINKS METHODS ====================
    
    @Step("Get all homepage template images")
    public List<WebElement> getHomeTemplateImages() {
        wait.until(ExpectedConditions.presenceOfElementLocated(homeTemplateImagesLocator));
        return driver.findElements(homeTemplateImagesLocator);
    }
    
    @Step("Click on homepage image: {imageName}")
    public void clickHomeTemplateImage(WebElement image, String imageName) {
        js.executeScript("arguments[0].click();", image);
        System.out.println("🖱️ Clicked on homepage image: " + imageName);
    }
    
    @Step("Get all footer links")
    public List<WebElement> getFooterLinks() {
        scrollDown(3200);
        wait.until(ExpectedConditions.presenceOfElementLocated(footerLinksLocator));
        return driver.findElements(footerLinksLocator);
    }
    
    @Step("Click on footer link: {linkText}")
    public void clickFooterLink(WebElement link, String linkText) {
        actions.moveToElement(link).perform();
        link.click();
        System.out.println("🖱️ Clicked on footer link: " + linkText);
    }
    
    // ==================== UTILITY METHODS ====================
    
    @Step("Scroll down by {pixels} pixels")
    public void scrollDown(int pixels) {
        js.executeScript("window.scrollBy(0," + pixels + ");", "");
        System.out.println("⬇️ Scrolled down by " + pixels + " pixels");
    }
    
    @Step("Scroll to element")
    private void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }
    
    @Step("Hover over menu")
    private void hoverOverMenu(WebElement element) {
        js.executeScript(
            "arguments[0].dispatchEvent(new MouseEvent('mouseenter', {bubbles:true}));",
            element
        );
    }
    
    @Step("Click on submenu")
    private void clickSubMenu(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }
    
    @Step("Verify page loaded with image")
    public boolean verifyPageLoadWithImage() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(pageLoadImageLocator));
            System.out.println("✅ Page loaded successfully");
            return true;
        } catch (TimeoutException e) {
            System.out.println("❌ Page failed to load");
            return false;
        }
    }
    
    @Step("Get page title")
    public String getPageTitle() {
        String title = driver.getTitle();
        System.out.println("📄 Page title: " + title);
        return title;
    }
    
    @Step("Check if page is 404 error")
    public boolean isPage404Error() {
        String title = getPageTitle();
        return title.contains("404 page not found");
    }
    
    @Step("Wait for logo to be clickable")
    public void waitForLogoClickable() {
        wait.until(ExpectedConditions.elementToBeClickable(logoIcon));
    }
    
    @Step("Refresh page")
    public void refreshPage() {
        driver.navigate().refresh();
        System.out.println("🔄 Page refreshed");
    }
}
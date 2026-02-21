package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;
import org.testng.ITestContext;

import utils.CommonUtils;
import utils.ConfigReader;
import utils.TestListener;

import java.time.Duration;
import io.github.bonigarcia.wdm.WebDriverManager;

@Listeners(TestListener.class) // Attach the listener for screenshots
public class BaseTest {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> threadLocalWait = new ThreadLocal<>();
    
 // Add this to store current test case ID for the thread
    private static final ThreadLocal<String> threadLocalTestCaseId = new ThreadLocal<>();
    
    /**
     * Get driver instance for current thread (useful for parallel execution)
     */
    public WebDriver getDriver() {
        return threadLocalDriver.get();
    }
    
    /**
     * Get WebDriverWait for current thread
     */
    public WebDriverWait getWait() {
        return threadLocalWait.get();
    }
    
    /**
     *  Get current test case ID for the thread
     */
    public String getTestCaseId() {
        return threadLocalTestCaseId.get();
    }
    /**
     *  Set current test case ID for the thread
     */
    public void setTestCaseId(String testCaseId) {
        threadLocalTestCaseId.set(testCaseId);
    }
    /**
     * Initialize WebDriver based on browser from config
     */
    private WebDriver initializeDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean headless = ConfigReader.isHeadless();
        
        WebDriver webDriver;
        
        switch (browser) {
            case "chrome":
            	WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--remote-allow-origins=*");
                webDriver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                webDriver = new FirefoxDriver(firefoxOptions);
                webDriver.manage().window().maximize();
                break;
                
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                webDriver = new EdgeDriver(edgeOptions);
                webDriver.manage().window().maximize();
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        // Set implicit wait
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        
        // Set page load timeout
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        
        return webDriver;
    }
    
    /**
     * Setup method runs before each test method
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestContext context) {
        driver = initializeDriver();
        threadLocalDriver.set(driver);
        
        // Initialize WebDriverWait
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
        threadLocalWait.set(wait);
        
        // Navigate to application URL
        driver.get(ConfigReader.getAppUrl());
        
        // Store driver in TestNG context for potential use across tests
        context.setAttribute("WebDriver", driver);
        
        System.out.println("=== Test Started: " + context.getName() + " ===");
    }
    
    /**
     * Teardown method runs after each test method
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (threadLocalDriver.get() != null) {
            threadLocalDriver.get().quit();
            threadLocalDriver.remove();
            threadLocalWait.remove();
        }
        System.out.println("=== Test Completed ===");
    }
    
    /**
     * Navigate to specific URL (overrides base URL)
     */
    public void navigateTo(String url) {
        getDriver().navigate().to(url);
    }
    
    /**
     * Refresh current page
     */
    public void refreshPage() {
        getDriver().navigate().refresh();
    }
    
    /**
     * Get current page title
     */
    public String getPageTitle() {
        return getDriver().getTitle();
    }
    
    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }
    
    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        getWait().until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
    }
    
    
    
    /**
     * Setup runs once before all tests
     */
    @BeforeSuite
    public void beforeSuite() {
        System.out.println("=== Test Suite Started ===");
        // Setup any suite-level configurations
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac") || os.contains("linux")) {
            // Set permissions for driver executables if needed
            System.out.println("Running on: " + os);
        }
    }
    
    /**
     * Cleanup after all tests
     */
    @AfterSuite
    public void afterSuite() {
        System.out.println("=== Test Suite Completed ===");
    }
}
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();
    
    static {
        try {
            FileInputStream file = new FileInputStream("src/test/resources/config.properties");
            properties.load(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file!");
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getBrowser() {
        return getProperty("browser");
    }
    
    public static String getAppUrl() {
        return getProperty("app.url");
    }
    
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait"));
    }
    
    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait"));
    }
    
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

	public static int getPageLoadTimeout() {
		// TODO Auto-generated method stub
		return Integer.parseInt(getProperty("page.load.timeout"));
	}
}
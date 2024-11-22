package com.sauce_demo.base;

import com.sauce_demo.Utils.PropertyParser;
import com.sauce_demo.actiondriver.Action;
import com.sauce_demo.constants.Constants;
import com.sauce_demo.constants.FilePathConstants;
import com.sauce_demo.constants.LogMessages;
import com.sauce_demo.pagekeys.LoginPageKeys;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Properties;

/**
 * Base class that handles the initialization of WebDriver and loading of configuration properties.
 * It selects the appropriate browser and launches the application URL based on configuration settings.
 *
 */
public class BaseClass {

    /**
     * Properties object to hold configuration settings from external property files.
     */

    public Logger report = Logger.getLogger(BaseClass.class);
    /**
     * WebDriver object to manage the browser instance.
     */
    public static WebDriver driver;

    /**
     * PropertyParser instance to parse and retrieve property values for the login page.
     */
    private PropertyParser loginPageProperties;

    /**
     * Constructor that initializes the PropertyParser for login page properties.
     *
     */
    public BaseClass() {
        loginPageProperties = new PropertyParser(FilePathConstants.LOGIN_PAGE_PATH);
    }

    /**
     * Launches the application by selecting the browser from the configuration and setting up WebDriver.
     * Configures implicit wait, page load timeout, and maximizes the browser window.
     *
     * The method uses the WebDriverManager to handle browser driver setups automatically.
     * The supported browsers include Chrome, Firefox, and Edge.
     *
     */

    public void LaunchApp() {

        // Set up the WebDriver for Chrome browser
        WebDriverManager.chromedriver().setup();

        // Get the browser name from the properties file
        String browserName = loginPageProperties.getPropertyValue(LoginPageKeys.Browser);

        // Select the WebDriver based on the configured browser
        if (browserName.equalsIgnoreCase(Constants.CHROME)) {
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase(Constants.FIREFOX)) {
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase(Constants.EDGE)) {
            driver = new EdgeDriver();
        }


        Action.implicitWait(Constants.TIME_OUTS);
        Action.pageLoadTimeOut(Constants.PAGE_LOAD);
        report.info(LogMessages.OPEN_BROWSER);
        Action.launchUrl(driver, loginPageProperties.getPropertyValue(LoginPageKeys.URL));
        report.info("url opened");
        report.info(LogMessages.MAXIMIZE_WINDOW);
        driver.manage().window().maximize();
    }
}

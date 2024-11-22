package com.sauce_demo;

import com.sauce_demo.Utils.ExtentManager;
import com.sauce_demo.base.BaseClass;

import com.sauce_demo.constants.LogMessages;
import com.sauce_demo.pages.LoginPage;
import com.sauce_demo.pages.ProductPage;
import com.sauce_demo.reusableFiles.TestUtil;
import org.testng.annotations.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

import static com.sauce_demo.base.BaseClass.*;

/**
 * This class contains test scripts for the SauceDemo application.
 * It includes tests for login, product sorting, and checkout functionalities.
 * It is used to verify the login functionality, test different sorting options for products,
 * and add products to the cart to verify the checkout process.
 */
public class SauceDemoTestScript {

    private static final Logger logger = Logger.getLogger(SauceDemoTestScript.class);

    BaseClass baseClass = new BaseClass();
    LoginPage loginPage = new LoginPage();
    ProductPage productPage = new ProductPage();

    /**
     * Sets up the extent reporting before the suite begins execution.
     * This method initializes the ExtentReports and starts the report generation.
     */
    @BeforeSuite
    public void beforeSuite() {
        logger.info(LogMessages.TEST_SUITE_START);
        ExtentManager.setExtent();
    }

    /**
     * Launches the SauceDemo application before the test class execution begins.
     * This method calls the `LaunchApp` method in `BaseClass` to start the application.
     */
    @BeforeClass
    public void launchApp() {
        logger.info(LogMessages.APP_LAUNCH);
        baseClass.LaunchApp();
    }

    /**
     * Quits the driver after the execution of the test class.
     * This method is used to clean up and close the browser after the tests are complete.
     */
    @AfterClass
    public void tearDown() {
        logger.info(LogMessages.DRIVER_QUIT);
        driver.quit();
    }

    /**
     * Provides test data for login tests using a DataProvider.
     * Retrieves data from an Excel file using `TestUtil`.
     *
     * @return an iterator of Object arrays containing test data.
     */
    @DataProvider(name = "getData")
    public Iterator<Object[]> getTestData() {
        ArrayList<Object[]> testData = TestUtil.getDataFromExcel();
        return testData.iterator();
    }

    /**
     * Tests the login functionality with multiple sets of data.
     * Verifies if different login inputs work as expected.
     *
     * @param username the username for the login attempt.
     * @param password the password for the login attempt.
     */
    @Test(priority = 1, dataProvider = "getData")
    @Parameters({"username", "password"})
    public void testLoginToSauceDemo(String username, String password) {
        logger.info(LogMessages.TEST_LOGIN + username);
        loginPage.enterLoginCredentials(username, password);
    }

    /**
     * Verifies different sorting options for products on the home page.
     * Checks sorting by Name (A to Z), Name (Z to A), Price (Low to High), and Price (High to Low).
     * This method tests all sorting options and ensures they are working as expected.
     *
     * @throws InterruptedException if thread interruption occurs during the test execution.
     */
    @Test(priority = 2, dependsOnMethods = "testLoginToSauceDemo")
    public void testAllSortingOptionsForProducts() throws InterruptedException {
        logger.info(LogMessages.SORTING_TEST);
        productPage.testNameSortAtoZ();
        productPage.testNameSortZtoA();
        productPage.testPriceSortLowToHigh();
        productPage.testPriceSortHighToLow();
    }

    /**
     * Verifies the checkout functionality by adding products to the cart.
     * Ensures the selected products are correctly added to the cart and verifies their presence in the cart.
     */
    @Test(priority = 3, dependsOnMethods = "testAllSortingOptionsForProducts")
    public void testAddAndVerifyCartProduct() {
        logger.info(LogMessages.ADD_TO_CART_TEST);
        productPage.selectProduct();
        productPage.clickCartContainer();
        productPage.verifyProductInCart();
    }

    /**
     * Closes the extent reporting before the suite execution ends.
     * This method is used to flush the results of the reports after all tests have been executed.
     */
    @AfterSuite
    public void afterSuite() {
        logger.info(LogMessages.TEST_SUITE_END);
        ExtentManager.endReport();
    }
}
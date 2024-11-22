package com.sauce_demo.actiondriver;


import com.sauce_demo.constants.FilePathConstants;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import static com.sauce_demo.base.BaseClass.driver;


public class Action {
    static Logger log = Logger.getLogger(Action.class);
    private List<String> clickedProductNames = new ArrayList<>();


    /**
     * Attempts to launch the provided URL in the browser and logs the result.
     *
     * @param driver the WebDriver instance used to interact with the browser
     * @param url    the URL to navigate to
     */
    public static void launchUrl(WebDriver driver, String url) {
        try {

            driver.navigate().to(url);
            log.info("Successfully launched \"" + url + "\".");
        } catch (Exception e) {
            log.error("Failed to launch \"" + url + "\".");
        }
    }

    public static String getPageTitle(WebDriver driver) {

        return driver.getTitle();
    }

    public static void implicitWait(int timeOut) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut));
    }

    public static void pageLoadTimeOut(int timeOut) {
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeOut));
    }

    public static String screenShot(WebDriver driver, String filename) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
        File source = takesScreenshot.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + File.separator + "ScreenShots" + File.separator + filename + "_" + dateName + ".png";

        try {
            FileUtils.copyFile(source, new File(destination));
        } catch (Exception e) {
            e.getMessage();
        }

        return FilePathConstants.REPORT_HOME + dateName + ".png";
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date());
    }


    public void selectByVisibleText(String locatorType, String locatorValue, String value) {
        boolean isSelected = false;
        try {
            // Locate the dropdown element
            By locator = getByLocator(locatorType, locatorValue);
            WebElement dropdownElement = driver.findElement(locator);

            // Create a Select instance and select by value
            Select dropdown = new Select(dropdownElement);
            dropdown.selectByVisibleText(value);

            isSelected = true;
            log.info("Option selected by value: " + value);
        } catch (Exception e) {
            log.error("Failed to select option by value: " + value + ". Error: " + e.getMessage());
        } finally {
            if (isSelected) {
                log.info("Selection operation completed successfully.");
            } else {
                log.info("Selection operation failed.");
            }
        }
    }


    /**
     * Waits for the presence of an element based on locator type.
     *
     * @param locatorType      The type of locator (e.g., XPATH, ID, CSS, etc.).
     * @param locatorValue     The value of the locator (e.g., XPath string, CSS selector, etc.).
     * @param timeoutInSeconds Timeout in seconds to wait for the element.
     * @throws IllegalArgumentException             If an unsupported locator type is passed.
     * @throws org.openqa.selenium.TimeoutException If the element is not present within the timeout.
     */
    public void waitForElementPresence(String locatorType, String locatorValue, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        By locator = getByLocator(locatorType, locatorValue);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Helper method to get By locator based on type and value.
     *
     * @param locatorType  The type of locator (e.g., XPATH, ID, CSS, etc.).
     * @param locatorValue The value of the locator.
     * @return The corresponding By object.
     * @throws IllegalArgumentException If an unsupported locator type is passed.
     */
    private By getByLocator(String locatorType, String locatorValue) {
        return switch (locatorType.toUpperCase()) {
            case "XPATH" -> By.xpath(locatorValue);
            case "ID" -> By.id(locatorValue);
            case "CSS" -> By.cssSelector(locatorValue);
            case "NAME" -> By.name(locatorValue);
            case "CLASS_NAME" -> By.className(locatorValue);
            case "TAG_NAME" -> By.tagName(locatorValue);
            case "LINK_TEXT" -> By.linkText(locatorValue);
            case "PARTIAL_LINK_TEXT" -> By.partialLinkText(locatorValue);
            default -> throw new IllegalArgumentException("Unsupported locator type: " + locatorType);
        };
    }

    /**
     * Enters text into a text field identified by the locator type and value.
     *
     * @param locatorType  The type of locator (e.g., XPATH, ID, CSS, etc.).
     * @param locatorValue The value of the locator.
     * @param text         The text to enter into the text field.
     * @throws IllegalArgumentException If an unsupported locator type is passed.
     */
    public void enterTextField(String locatorType, String locatorValue, String text) {
        By locator = getByLocator(locatorType, locatorValue);
        WebElement textField = driver.findElement(locator);
        textField.clear(); // Clear the text field before entering text
        textField.sendKeys(text);
    }

    /**
     * Performs a click action on an element identified by the locator type and value.
     *
     * @param locatorType  The type of locator (e.g., XPATH, ID, CSS, etc.).
     * @param locatorValue The value of the locator.
     * @throws IllegalArgumentException If an unsupported locator type is passed.
     */
    public void performClick(String locatorType, String locatorValue) {
        By locator = getByLocator(locatorType, locatorValue);
        WebElement element = driver.findElement(locator);
        element.click();
    }

    /**
     * Clicks on up to a specified number of elements matching a given XPath.
     *
     * @param locatorType    The type of locator (e.g., XPATH).
     * @param locatorValue   The value of the locator.
     * @param numberOfClicks The number of elements to click.
     */
    public void clickMultipleElements(String locatorType, String locatorValue, String AddToCartButtonFirst, String AddToCartButtonSecond, int numberOfClicks) {
        // Get locator and find elements
        By locator = getByLocator(locatorType, locatorValue);
        List<WebElement> elements = driver.findElements(locator);

        // If there are fewer elements than required clicks, limit to available elements
        if (elements.size() < numberOfClicks) {
            numberOfClicks = elements.size();
        }

        // Shuffle the list to get random order
        Collections.shuffle(elements);

        // Click on the selected elements
        int clickCount = 0;
        for (WebElement element : elements) {
            if (clickCount < numberOfClicks) {
                // Get the product name
                String productName = element.getText();

                // Add product name to the list (this is for later verification)
                clickedProductNames.add(productName);

                // Construct the "Add to Cart" button locator based on product name
                By addToCart = getByLocator(locatorType, AddToCartButtonFirst + productName + AddToCartButtonSecond);
                WebElement addToCartButton = driver.findElement(addToCart);

                // Click the "Add to Cart" button
                addToCartButton.click();
                System.out.println("Clicked on product: " + productName);

                clickCount++;
            } else {
                break;
            }
        }

        // If fewer products were available than expected
        if (clickCount < numberOfClicks) {
            System.out.println("Only " + clickCount + " elements were available to click.");
        }
    }

    public List<String> getClickedProductNames() {
        return clickedProductNames;  // Return the list of clicked product names
    }

    public void verifyProductNamesInCart(List<String> clickedProductNames, String locatorType, String locatorValue) {
        // Find the names of products in the cart (update locator as necessary)
        By locator = getByLocator(locatorType, locatorValue);
        List<WebElement> cartItems = driver.findElements(locator);

        List<String> cartProductNames = new ArrayList<>();
        for (WebElement cartItem : cartItems) {
            String cartItemName = cartItem.findElement(locator).getText();
            cartProductNames.add(cartItemName);
        }

        // Verify that the clicked product names are in the cart
        for (String productName : clickedProductNames) {
            if (cartProductNames.contains(productName)) {
                System.out.println("Product " + productName + " is in the cart.");
            } else {
                System.out.println("Product " + productName + " is NOT in the cart.");
            }
        }

    }

    // Method to get product names and prices from the homepage
    public List<String> getProductNames() {
        List<String> productNames = new ArrayList<>();
        List<WebElement> productElements = driver.findElements(By.className("inventory_item_name"));
        for (WebElement product : productElements) {
            productNames.add(product.getText().trim());
        }
        return productNames;
    }

    // Method to get product prices from the homepage
    public List<Double> getProductPrices() {
        List<Double> productPrices = new ArrayList<>();
        List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().replace("$", "").trim();
            productPrices.add(Double.parseDouble(priceText));
        }
        return productPrices;
    }
}
      
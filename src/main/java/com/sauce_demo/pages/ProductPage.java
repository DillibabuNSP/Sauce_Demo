package com.sauce_demo.pages;

import com.sauce_demo.Utils.PropertyParser;
import com.sauce_demo.Utils.Log;
import com.sauce_demo.actiondriver.Action;
import com.sauce_demo.constants.Constants;
import com.sauce_demo.constants.FilePathConstants;
import com.sauce_demo.constants.LogMessages;
import com.sauce_demo.pagekeys.ProductPageKeys;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains methods for interacting with the product page and verifying its functionality.
 */
public class ProductPage {
    /**
     * Logger instance for reporting logs.
     */
    private static final Logger report = Log.getLogger(ProductPage.class);

    /**
     * Action instance for performing reusable actions.
     */
    private final Action action = new Action();

    /**
     * PropertyParser instance to parse and retrieve property values for the product page.
     */
    private final PropertyParser productPageProperties;

    /**
     * Constructor that initializes the PropertyParser for product page properties.
     */
    public ProductPage() {
        productPageProperties = new PropertyParser(FilePathConstants.PRODUCT_PAGE_PATH);
    }

    /**
     * Selects a specified number of products from the product list and adds them to the cart.
     * Logs information on product selection and handles any errors during the process.
     */
    public void selectProduct() {
        try {
            report.info(LogMessages.SELECTING_PRODUCTS_TO_ADD_TO_CART);
            String productNameXpath = productPageProperties.getPropertyValue(ProductPageKeys.PRODUCT_NAME_XPATH);
            String addToCartButtonFirstXpath = productPageProperties.getPropertyValue(ProductPageKeys.ADDTOCARTBUTTON_FIRST_XPATH);
            String addToCartButtonSecondXpath = productPageProperties.getPropertyValue(ProductPageKeys.ADDTOCARTBUTTON_SECOND_XPATH);
            action.waitForElementPresence(Constants.XPATH, productNameXpath, Constants.TIME_OUTS);
            action.clickMultipleElements(Constants.XPATH, productNameXpath, addToCartButtonFirstXpath, addToCartButtonSecondXpath, 3);
            report.info(LogMessages.PRODUCTS_SUCCESSFULLY_ADDED_TO_THE_CART);
        } catch (Exception e) {
            report.error(LogMessages.ERROR_WHILE_SELECTING_PRODUCTS + e.getMessage(), e);
        }
    }

    /**
     * Clicks on the cart container to navigate to the cart page.
     * Logs information about the cart navigation and handles any errors.
     */
    public void clickCartContainer() {
        try {
            report.info(LogMessages.NAVIGATING_TO_CART_CONTAINER);
            String cartContainerXpath = productPageProperties.getPropertyValue(ProductPageKeys.CARTCONTAINERXPATH);
            action.waitForElementPresence(Constants.XPATH, cartContainerXpath, Constants.TIME_OUTS);
            action.performClick(Constants.XPATH, cartContainerXpath);
            report.info(LogMessages.SUCCESSFULLY_NAVIGATED_TO_CART);
        } catch (Exception e) {
            report.error(LogMessages.ERROR_WHILE_CLICKING_CART_CONTAINER + e.getMessage(), e);
        }
    }

    /**
     * Verifies that the selected products are present in the cart.
     * Logs the verification process and any errors encountered during product verification.
     */
    public void verifyProductInCart() {
        try {
            report.info(LogMessages.VERIFYING_PRODUCTS_IN_CART);
            String productNameInCartXpath = productPageProperties.getPropertyValue(ProductPageKeys.PRODUCTNAMEINCARTXPATH);
            action.waitForElementPresence(Constants.XPATH, productNameInCartXpath, Constants.TIME_OUTS);
            action.verifyProductNamesInCart(action.getClickedProductNames(), Constants.XPATH, productNameInCartXpath);
            report.info(LogMessages.PRODUCT_VERIFICATION_SUCCESSFUL);
        } catch (Exception e) {
            report.error(LogMessages.ERROR_DURING_PRODUCT_VERIFICATION + e.getMessage(), e);
        }
    }

    /**
     * Selects a sorting option from the sort dropdown menu on the product page.
     * Logs the selection process and handles any errors encountered.
     *
     * @param option The sorting option to select (e.g., "Name (A to Z)", "Price (low to high)").
     */
    public void selectSortingOption(String option) {
        try {
            report.info(LogMessages.SELECTING_SORTING_OPTION + option);
            String sortOptionXpath = productPageProperties.getPropertyValue(ProductPageKeys.SORTFILTERXPATH);
            action.waitForElementPresence(Constants.XPATH, sortOptionXpath, Constants.TIME_OUTS);
            action.performClick(Constants.XPATH, sortOptionXpath);
            action.selectByVisibleText(Constants.XPATH, sortOptionXpath, option);
            report.info(LogMessages.SORTING_OPTION_SELECTED_SUCCESSFULLY + option);
        } catch (Exception e) {
            report.error(LogMessages.ERROR_WHILE_SELECTING_SORTING_OPTION + e.getMessage(), e);
        }
    }

    /**
     * Verifies that the product names are sorted in ascending (A to Z) order.
     * Logs the process of verifying the sort order and asserts that the products are sorted correctly.
     */
    public void testNameSortAtoZ() {
        report.info(LogMessages.TESTING_NAME_SORT_A_TO_Z);
        selectSortingOption("Name (A to Z)");

        List<String> productNames = action.getProductNames();
        List<String> sortedProductNames = new ArrayList<>(productNames);
        Collections.sort(sortedProductNames);

        Assert.assertEquals(productNames, sortedProductNames, LogMessages.NAME_SORT_A_TO_Z_VERIFIED);
        report.info(LogMessages.NAME_SORT_A_TO_Z_VERIFIED);
    }

    /**
     * Verifies that the product names are sorted in descending (Z to A) order.
     * Logs the process of verifying the sort order and asserts that the products are sorted correctly.
     */
    public void testNameSortZtoA() {
        report.info(LogMessages.TESTING_NAME_SORT_Z_TO_A);
        selectSortingOption("Name (Z to A)");

        List<String> productNames = action.getProductNames();
        List<String> sortedProductNames = new ArrayList<>(productNames);
        Collections.sort(sortedProductNames, Collections.reverseOrder());

        Assert.assertEquals(productNames, sortedProductNames, LogMessages.NAME_SORT_Z_TO_A_VERIFIED);
        report.info(LogMessages.NAME_SORT_Z_TO_A_VERIFIED);
    }

    /**
     * Verifies that the product prices are sorted in ascending (low to high) order.
     * Logs the process of verifying the sort order and asserts that the products are sorted correctly.
     */
    public void testPriceSortLowToHigh() {
        report.info(LogMessages.TESTING_PRICE_SORT_LOW_TO_HIGH);
        selectSortingOption("Price (low to high)");

        List<Double> productPrices = action.getProductPrices();
        List<Double> sortedProductPrices = new ArrayList<>(productPrices);
        Collections.sort(sortedProductPrices);

        Assert.assertEquals(productPrices, sortedProductPrices, LogMessages.PRICE_SORTING_LOW_TO_HIGH_VERIFIED);
        report.info(LogMessages.PRICE_SORTING_LOW_TO_HIGH_VERIFIED);
    }

    /**
     * Verifies that the product prices are sorted in descending (high to low) order.
     * Logs the process of verifying the sort order and asserts that the products are sorted correctly.
     */
    public void testPriceSortHighToLow() {
        report.info(LogMessages.TESTING_PRICE_SORT_HIGH_TO_LOW);
        selectSortingOption("Price (high to low)");

        List<Double> productPrices = action.getProductPrices();
        List<Double> sortedProductPrices = new ArrayList<>(productPrices);
        Collections.sort(sortedProductPrices, Collections.reverseOrder());

        Assert.assertEquals(productPrices, sortedProductPrices, LogMessages.PRICE_SORTING_HIGH_TO_LOW_VERIFIED_SUCCESSFULLY);
        report.info(LogMessages.PRICE_SORTING_HIGH_TO_LOW_VERIFIED_SUCCESSFULLY);
    }
}

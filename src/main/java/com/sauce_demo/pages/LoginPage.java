package com.sauce_demo.pages;

import com.sauce_demo.Utils.PropertyParser;
import com.sauce_demo.actiondriver.Action;
import com.sauce_demo.constants.Constants;
import com.sauce_demo.constants.FilePathConstants;
import com.sauce_demo.constants.LogMessages;
import com.sauce_demo.pagekeys.LoginPageKeys;
import org.apache.log4j.Logger;

/**
 * This class contains methods to interact with the Login page of the SauceDemo application.
 * It includes methods to enter login credentials and click the login button.
 * The login page elements are identified and interacted with using property keys.
 */
public class LoginPage {

    /**
     * Logger instance to log actions and messages related to the login page.
     */
    public Logger report = Logger.getLogger(LoginPage.class);

    /**
     * Action instance for performing reusable actions like waiting for elements,
     * entering text, and clicking elements.
     */
    Action action = new Action();

    /**
     * PropertyParser instance to load and retrieve login page element locators from properties file.
     */
    private PropertyParser loginPageProperties;

    /**
     * Constructor to initialize the PropertyParser for loading login page properties.
     * It loads the property file that contains the locators for elements on the login page.
     */
    public LoginPage() {
        loginPageProperties = new PropertyParser(FilePathConstants.LOGIN_PAGE_PATH);
    }

    /**
     * Enters login credentials (username and password) into the respective fields and clicks the login button.
     *
     * @param userName The username to be entered in the login field.
     * @param passWord The password to be entered in the password field.
     */
    public void enterLoginCredentials(String userName, String passWord) {

        // Log the action of entering the username
        report.info(LogMessages.ENTERUSERNAME);

        // Retrieve the XPath for the username field and interact with it
        String userNameXpath = loginPageProperties.getPropertyValue(LoginPageKeys.USER_NAME);
        action.waitForElementPresence(Constants.ID, userNameXpath, Constants.TIME_OUTS);
        action.enterTextField(Constants.ID, userNameXpath, userName);

        // Log the action of entering the password
        report.info(LogMessages.ENTERPASSWORD);

        // Retrieve the XPath for the password field and interact with it
        String passWordXpath = loginPageProperties.getPropertyValue(LoginPageKeys.PASS_WORD);
        action.waitForElementPresence(Constants.ID, passWordXpath, Constants.TIME_OUTS);
        action.enterTextField(Constants.ID, passWordXpath, passWord);

        // Log the action of clicking the login button
        report.info(LogMessages.CLICKLOGINBUTTON);

        // Retrieve the XPath for the login button and perform the click action
        String loginButtonXpath = loginPageProperties.getPropertyValue(LoginPageKeys.LOGIN_BUTTON);
        action.waitForElementPresence(Constants.ID, loginButtonXpath, Constants.TIME_OUTS);
        action.performClick(Constants.ID, loginButtonXpath);
    }
}

package com.sauce_demo.Utils;
/*
user: To perform Log Action by this class
Description:Dillibabu
*/

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.sauce_demo.actiondriver.Action;
import com.sauce_demo.constants.FilePathConstants;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ExtentManager {

    private static final Logger logger = Logger.getLogger(ExtentManager.class);
    public static ExtentHtmlReporter htmlReporter;
    public static ExtentReports extent;

    /**
     * Initializes the ExtentReports with configuration and file path.
     */
    public static void setExtent() {

        // Set the report file path
        String reportPath = FilePathConstants.REPORT_HOME + "MyReport_" + Action.getCurrentTime() + ".html";
        htmlReporter = new ExtentHtmlReporter(reportPath);

        try {
            // Load the extent-config.xml for additional configuration
            htmlReporter.loadXMLConfig(FilePathConstants.CONFIGURATION_HOME + "extent-config.xml");
        } catch (IOException e) {
            logger.error("Error loading extent-config.xml: " + e.getMessage(), e);
            return; // Ensure that execution stops if config file loading fails
        }

        // Set configurations for the HTML report
        htmlReporter.config().setDocumentTitle("Sauce Demo Application");
        htmlReporter.config().setReportName("Sauce Demo Automation Log");
        htmlReporter.config().setTheme(Theme.DARK);

        // Initialize ExtentReports and attach the reporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        // Set system-level info
        extent.setSystemInfo("HostName", "MyHost");
        extent.setSystemInfo("ProjectName", "Sauce Demo WebSite");
        extent.setSystemInfo("Tester", "DilliBabu");
        extent.setSystemInfo("OS", "Win11");
        extent.setSystemInfo("Browser", "Chrome");
    }

    /**
     * Finalizes the report by flushing the logs and saving the HTML report.
     */
    public static void endReport() {
        extent.flush();
    }
}

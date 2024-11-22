package com.sauce_demo.Utils;

import org.apache.log4j.Logger;

/**
 * Utility class for managing logs using Log4j.
 */
public class Log {

    // Log4j Logger instance
    private static Logger log;

    /**
     * Initializes the Logger for a specific class.
     *
     * @param clazz The class for which the logger is initialized.
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

    /**
     * Logs the start of a test case.
     *
     * @param testCaseName Name of the test case.
     */
    public static void startTestCase(String testCaseName) {
        log.info("=====================================" + testCaseName + " TEST START =========================================");
    }

    /**
     * Logs the end of a test case.
     *
     * @param testCaseName Name of the test case.
     */
    public static void endTestCase(String testCaseName) {
        log.info("=====================================" + testCaseName + " TEST END =========================================");
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void warn(String message) {
        log.warn(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void fatal(String message) {
        log.fatal(message);
    }

    public static void debug(String message) {
        log.debug(message);
    }
}

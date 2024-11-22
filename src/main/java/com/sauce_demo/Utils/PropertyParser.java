package com.sauce_demo.Utils;

import com.sauce_demo.constants.FilePathConstants;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class contains methods for reading properties files.
 */
public class PropertyParser {

    private Properties properties;

    /**
     * This is the constructor that loads the properties file.
     *
     * @param propertyFilePath the path of the properties file to load
     */
    public PropertyParser(String propertyFilePath) {
        properties = new Properties();
        loadProperties(propertyFilePath);

        // Initialize log4j properties for logging configuration
        loadLog4jProperties();
    }

    private void loadLog4jProperties() {
        // Ensure the log4j configuration file is loaded from the classpath
        String log4jConfigPath = FilePathConstants.LOG4J_PROPERTIES_FILE_PATH; // Set the correct path
        PropertyConfigurator.configure(log4jConfigPath); // Configure log4j using properties
    }

    /**
     * This method loads properties file.
     *
     * @param propertyFilePath the path to the properties file
     */
    private void loadProperties(String propertyFilePath) {
        try (FileInputStream fileInputStream = new FileInputStream(propertyFilePath)) {
            properties.load(fileInputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * This method returns a property value from the properties file based on the given key.
     *
     * @param key the key to fetch the property value
     * @return the property value associated with the key
     */
    public String getPropertyValue(String key) {
        return properties.getProperty(key);
    }
}

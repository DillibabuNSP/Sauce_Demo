package com.sauce_demo.reusableFiles;

import com.sauce_demo.constants.Constants;
import com.sauce_demo.constants.FilePathConstants;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * This class provides utility methods to interact with Excel files.
 * It is primarily used for reading test data (username and password) from an Excel file.
 */
public class TestUtil {
    /**
     * Logger instance to log actions and messages related to the test utility methods.
     */
    static Logger log = Logger.getLogger(TestUtil.class);

    /**
     * Xls_Reader instance used for reading data from an Excel file.
     */
    static Xls_Reader reader;

    /**
     * Retrieves test data (username and password) from an Excel file.
     * It reads the data from the specified sheet and returns it as an ArrayList of Object arrays.
     *
     * @return An ArrayList of Object arrays containing test data, where each array holds a username and password.
     */
    public static ArrayList<Object[]> getDataFromExcel() {
        ArrayList<Object[]> myData = new ArrayList<Object[]>();

        try {
            // Initialize the reader with the Excel file path
            reader = new Xls_Reader(FilePathConstants.EXCEL_INPUT_PATH);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        // Get the total number of rows in the sheet
        int count = reader.getRowCount(Constants.SHEET_1);

        // Loop through rows starting from row 2 (assuming row 1 contains headers)
        for (int rowNum = 2; rowNum <= reader.getRowCount(Constants.SHEET_1); rowNum++) {
            // Retrieve username and password from the current row
            String username = reader.getCellData(Constants.SHEET_1, Constants.COLUMN_USERNAME, rowNum);
            String password = reader.getCellData(Constants.SHEET_1, Constants.COLUMN_PASSWORD, rowNum);

            // Store the username and password in an Object array
            Object[] ob = {username, password};

            // Add the Object array to the ArrayList
            myData.add(ob);
        }

        // Return the ArrayList containing all the test data
        return myData;
    }
}

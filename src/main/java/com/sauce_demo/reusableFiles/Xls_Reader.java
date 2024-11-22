package com.sauce_demo.reusableFiles;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellBase;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import static org.apache.poi.ss.usermodel.DateUtil.getJavaDate;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;

public class Xls_Reader {
    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    private XSSFWorkbook workbook = null;
    private XSSFSheet sheet = null;
    private XSSFRow row = null;
    private XSSFCell cell = null;

    public Xls_Reader(String path) {

        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    // returns the row count in a sheet

    public int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) return 0;
        else {
            sheet = workbook.getSheetAt(index);
            int number = sheet.getLastRowNum() + 1;
            return number;
        }

    }

    /**
     * Code has been updated as per new POI version - 4.x.x
     *
     * @param sheetName
     * @param rowNum
     * @return
     */
    // returns the data from a cell
    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            if (rowNum <= 0) return "";

            int index = workbook.getSheetIndex(sheetName);
            int col_Num = -1;
            if (index == -1) return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                // System.out.println(row.getCell(i).getStringCellValue().trim());
                if (row.getCell(i).getStringCellValue().trim().equals(colName.trim())) col_Num = i;
            }
            if (col_Num == -1) return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null) return "";
            cell = row.getCell(col_Num);

            if (cell == null) return "";

            //System.out.println(cell.getCellType().name());
            //
            if (cell.getCellType().name().equals("STRING")) return cell.getStringCellValue();

                // if (cell.getCellType().STRING != null)

                // if(cell.getCellType()==Xls_Reader.CELL_TYPE_STRING)
                // return cell.getStringCellValue();
            else if ((cell.getCellType().name().equals("NUMERIC")) || (cell.getCellType().name().equals("FORMULA"))) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;

                    // System.out.println(cellText);

                }

                return cellText;
            } else if (cell.getCellType().BLANK != null) return "";
            else return String.valueOf(cell.getBooleanCellValue());

        } catch (Exception e) {

            e.printStackTrace();
            return "row " + rowNum + " or column " + colName + " does not exist in xls";
        }
    }

    /**
     * Code has been updated as per new POI version - 4.x.x
     *
     * @param sheetName
     * @param colNum
     * @param rowNum
     * @return
     */
    // returns the data from a cell
    public String getCellData(String sheetName, int colNum, int rowNum) {
        try {
            if (rowNum <= 0) return "";

            int index = workbook.getSheetIndex(sheetName);

            if (index == -1) return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null) return "";
            cell = row.getCell(colNum);
            if (cell == null) return "";

            //
            if (cell.getCellType().name().equals("STRING")) return cell.getStringCellValue();

                //
                // if (cell.getCellType().STRING != null)
                // return cell.getStringCellValue();
            else if ((cell.getCellType().name().equals("NUMERIC")) || (cell.getCellType().name().equals("FORMULA"))) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

                    // System.out.println(cellText);

                }

                return cellText;
            } else if (cell.getCellType().BLANK != null) return "";
            else return String.valueOf(cell.getBooleanCellValue());
        } catch (Exception e) {

            e.printStackTrace();
            return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
        }
    }

    // returns true if data is set successfully else false
    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            if (rowNum <= 0) return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1) return false;

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                // System.out.println(row.getCell(i).getStringCellValue().trim());
                if (row.getCell(i).getStringCellValue().trim().equals(colName)) colNum = i;
            }
            if (colNum == -1) return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null) row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null) cell = row.createCell(colNum);

            // cell style
            // CellStyle cs = workbook.createCellStyle();
            // cs.setWrapText(true);
            // cell.setCellStyle(cs);
            cell.setCellValue(data);

            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    // returns true if data is set successfully else false
    // public boolean setCellData(String sheetName,String colName,int rowNum,
    // String data,String url){
    // //System.out.println("setCellData setCellData******************");
    // try{
    // fis = new FileInputStream(path);
    // workbook = new XSSFWorkbook(fis);
    //
    // if(rowNum<=0)
    // return false;
    //
    // int index = workbook.getSheetIndex(sheetName);
    // int colNum=-1;
    // if(index==-1)
    // return false;
    //
    //
    // sheet = workbook.getSheetAt(index);
    // //System.out.println("A");
    // row=sheet.getRow(0);
    // for(int i=0;i<row.getLastCellNum();i++){
    // //System.out.println(row.getCell(i).getStringCellValue().trim());
    // if(row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
    // colNum=i;
    // }
    //
    // if(colNum==-1)
    // return false;
    // sheet.autoSizeColumn(colNum);
    // row = sheet.getRow(rowNum-1);
    // if (row == null)
    // row = sheet.createRow(rowNum-1);
    //
    // cell = row.getCell(colNum);
    // if (cell == null)
    // cell = row.createCell(colNum);
    //
    // cell.setCellValue(data);
    // XSSFCreationHelper createHelper = workbook.getCreationHelper();
    //
    // //cell style for hyperlinks
    // //by default hypelrinks are blue and underlined
    // CellStyle hlink_style = workbook.createCellStyle();
    // XSSFFont hlink_font = workbook.createFont();
    // hlink_font.setUnderline(XSSFFont.U_SINGLE);
    // hlink_font.setColor(IndexedColors.BLUE.getIndex());
    // hlink_style.setFont(hlink_font);
    // //hlink_style.setWrapText(true);
    //
    // XSSFHyperlink link = createHelper.createHyperlink(Xls_Reader.LINK_FILE);
    // link.setAddress(url);
    // cell.setHyperlink(link);
    // cell.setCellStyle(hlink_style);
    //
    // fileOut = new FileOutputStream(path);
    // workbook.write(fileOut);
    //
    // fileOut.close();
    //
    // }
    // catch(Exception e){
    // e.printStackTrace();
    // return false;
    // }
    // return true;
    // }

    // returns true if sheet is created successfully else false
    public boolean addSheet(String sheetname) {

        FileOutputStream fileOut;
        try {
            workbook.createSheet(sheetname);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // returns true if sheet is removed successfully else false if sheet does
    // not exist
    public boolean removeSheet(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) return false;

        FileOutputStream fileOut;
        try {
            workbook.removeSheetAt(index);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // returns true if column is created successfully
    public boolean addColumn(String sheetName, String colName) {
        // System.out.println("**************addColumn*********************");

        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) return false;

            XSSFCellStyle style = workbook.createCellStyle();
            // style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            // style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            if (row == null) row = sheet.createRow(0);

            // cell = row.getCell();
            // if (cell == null)
            // System.out.println(row.getLastCellNum());
            if (row.getLastCellNum() == -1) cell = row.createCell(0);
            else cell = row.createCell(row.getLastCellNum());

            cell.setCellValue(colName);
            cell.setCellStyle(style);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    // removes a column and all the contents
    public boolean removeColumn(String sheetName, int colNum) {
        try {
            if (!isSheetExist(sheetName)) return false;
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            XSSFCellStyle style = workbook.createCellStyle();
            // style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            XSSFCreationHelper createHelper = workbook.getCreationHelper();
            // style.setFillPattern(XSSFCellStyle.NO_FILL);
            for (int i = 0; i < getRowCount(sheetName); i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    cell = row.getCell(colNum);
                    if (cell != null) {
                        cell.setCellStyle(style);
                        row.removeCell(cell);
                    }
                }
            }
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    // find whether sheets exists
    public boolean isSheetExist(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = workbook.getSheetIndex(sheetName.toUpperCase());
            if (index == -1) return false;
            else return true;
        } else return true;
    }

    // returns number of columns in a sheet
    public int getColumnCount(String sheetName) {
        // check if sheet exists
        if (!isSheetExist(sheetName)) return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if (row == null) return -1;

        return row.getLastCellNum();

    }

    // String sheetName, String testCaseName,String keyword ,String URL,String
    // message
    // public boolean addHyperLink(String sheetName,String
    // screenShotColName,String testCaseName,int index,String url,String
    // message){
    // //System.out.println("ADDING addHyperLink******************");
    //
    // url=url.replace('\\', '/');
    // if(!isSheetExist(sheetName))
    // return false;
    //
    // sheet = workbook.getSheet(sheetName);
    //
    // for(int i=2;i<=getRowCount(sheetName);i++){
    // if(getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)){
    // //System.out.println("**caught "+(i+index));
    // setCellData(sheetName, screenShotColName, i+index, message,url);
    // break;
    // }
    // }
    //
    //
    // return true;
    // }
    public int getCellRowNum(String sheetName, String colName, String cellValue) {

        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;

    }

    public void verifyDataInExcelBookAllSheets(XSSFWorkbook workbook1, XSSFWorkbook workbook2) {
        System.out.println("Verifying if both work books have same data.............");
        // Since we have already verified that both work books have same number of sheets so iteration can be done against any workbook sheet count
        int sheetCounts = workbook1.getNumberOfSheets();
        // So we will iterate through sheet by sheet
        for (int i = 0; i < sheetCounts; i++) {
            // Get sheet at same index of both work books
            XSSFSheet s1 = workbook1.getSheetAt(i);
            XSSFSheet s2 = workbook2.getSheetAt(i);
            System.out.println("*********** Sheet Name : " + s1.getSheetName() + "*************");
            // Iterating through each row
            int rowCounts = s1.getPhysicalNumberOfRows();
            for (int j = 0; j < rowCounts; j++) {
                // Iterating through each cell
                int cellCounts = s1.getRow(j).getPhysicalNumberOfCells();
                for (int k = 0; k < cellCounts; k++) {
                    // Getting individual cell
                    CellBase c1 = s1.getRow(j).getCell(k);
                    Cell c2 = s2.getRow(j).getCell(k);
                    // Since cell have types and need o use different methods
                    if (c1.getCellType().equals(c2.getCellType())) {
                        if (c1.getCellType() == CellType.STRING) {
                            String v1 = c1.getStringCellValue();
                            String v2 = c2.getStringCellValue();
                            Assert.assertEquals(v1, v2, "Cell values are different.....");
                            System.out.println("Its matched : " + v1 + " === " + v2);
                        }
                        if (c1.getCellType() == CellType.NUMERIC) {
                            // If cell type is numeric, we need to check if data is of Date type
                            if (isCellDateFormatted(c1) | isCellDateFormatted(c2)) {
                                // Need to use DataFormatter to get data in given style otherwise it will come as time stamp
                                DataFormatter df = new DataFormatter();
                                String v1 = df.formatCellValue(c1);
                                String v2 = df.formatCellValue(c2);
                                Assert.assertEquals(v1, v2, "Cell values are different.....");
                                System.out.println("Its matched : " + v1 + " === " + v2);
                            } else {
                                double v1 = c1.getNumericCellValue();
                                double v2 = c2.getNumericCellValue();
                                Assert.assertEquals(v1, v2, "Cell values are different.....");
                                System.out.println("Its matched : " + v1 + " === " + v2);
                            }
                        }
                        if (c1.getCellType() == CellType.BOOLEAN) {
                            boolean v1 = c1.getBooleanCellValue();
                            boolean v2 = c2.getBooleanCellValue();
                            Assert.assertEquals(v1, v2, "Cell values are different.....");
                            System.out.println("Its matched : " + v1 + " === " + v2);
                        }
                    } else {
                        // If cell types are not same, exit comparison
                        Assert.fail("Non matching cell type.");
                    }
                }
            }
        }
        System.out.println("Hurray! Both work books have same data.");
    }
}
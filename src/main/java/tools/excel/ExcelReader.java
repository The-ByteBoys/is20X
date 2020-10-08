package tools.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


/**
 *  The ExcelReader class, reads from the excel-documents located in src/main/java/tools/excel/excelDocuments.
 * @author Johannes Birkeland
 */
public class ExcelReader {

    XSSFSheet sheet;
    XSSFWorkbook wb;

    //empty constructor
    public ExcelReader() {}


    /**
     * This chooseDocument() takes a path the path of an excelDocument, construct a File object from it, and then sets the wb field to hold a new XSSFWorkbook based on that file.
     * @param path path to an excelFile
     * @throws IOException If the path leads to nowhere
     * @throws InvalidFormatException If the file is not of the type xlsx.
     */
    public void chooseDocument(String path) throws IOException, InvalidFormatException {
        File file  = new File(path);
        wb = new XSSFWorkbook(file);
    }

    /**
     * getSheet() sets sheet to be a sheet in the wb file
     * @param sheetIndex index of the sheet. 0 - 7
     * @throws NullPointerException if wb is not set.
     */
    public void getSheet(int sheetIndex) throws NullPointerException, IllegalArgumentException {
        sheet = wb.getSheetAt(sheetIndex);
    }


    public void closeWb() {
        try {
            wb.close();
        } catch (IOException e) {
            PrintWriter pw = new PrintWriter(System.out);
            pw.println("There is no workbook to close!");
            e.printStackTrace();
        }
   


    /**
     * getRowValues() gets all the values from one row in the excel sheet.
     * @param rowNumber the number of the row.
     * @return HashMap a hashMap that holds all the values assign to a key.
     */
    public  HashMap<String, Object> getRowValues(int rowNumber) {
        Row row = sheet.getRow(rowNumber + 3); //+3 to ignore the 3 first rows.
        Iterator<Cell> cell = row.cellIterator(); // For jumping to the next cell.
        HashMap<String, Object> rowValues = new HashMap<>(); //Holds the key as a string, and the value as a object. Can be converted to string and Integer later.
        ArrayList<String> keys = keyGenerator(); // An ArrayList with the keys witch are getting mapped with the values;

        // While loop for iterating through the cells of a row.
        int i = 0;
        while (cell.hasNext() && i < keys.size()) { //While it the row have cells left and i is less than the amount of keys/values.
            Cell c = cell.next();
            switch (c.getCellType()) {

                case NUMERIC: //If the value is a number its gonna get the numeric value and map it with a key.
                    rowValues.put(keys.get(i), c.getNumericCellValue());
                    break;

                case STRING: //If the value is a string its gonna get the string value and map it with a key.
                    rowValues.put(keys.get(i), c.getStringCellValue());
                    break;

                case BLANK: //If the value is blank, it is still gonna map it with a key. Because if not, the keys after will be mapped with the wrong values.
                    rowValues.put(keys.get(i), null);
            }
            i++;
        }

        return rowValues;
    }



    /**
     * keyGenerator() is used for generating the keys for the getRowValues(). The tests have many classes and different types of tests.
     * And because of that you need to auto generate different keys for the different classes.
     * @return ArrayList the ArrayList you will use in getRowValues().
     */
    private  ArrayList<String> keyGenerator(){
        Row row = sheet.getRow(0);
        String athleteClass = row.getCell(0).getStringCellValue(); //The first cell (AI) of the sheet contains the athlete class.
        String yearBclass = row.getCell(4).getStringCellValue(); //The b classes have some differences in the 2020 reports. So we need this field.
        ArrayList<String> keys = new ArrayList<>();

        //Boolean variables that check if the A1 contains something that can ID the athlete class.
        boolean senior = athleteClass.contains("senior");
        boolean a = athleteClass.contains("A");
        boolean b = athleteClass.contains("B"); //For the reports before 2020
        boolean b2020 = athleteClass.contains("B") && yearBclass.contains("2020"); //For the 2020 reports.
        boolean c = athleteClass.contains("C");
        boolean allClasses_C_Excluded = senior || a || b || b2020;


        //Arrays that contains keys for each class
        String[] seniorTest = {"5000Watt", "5000Tid", "2000Watt", "2000Tid", "60Watt", "liggroProsent", "liggroKg", "knebProsent", "knebKg", "antBev"};
        String[] aTest = {"5000Watt", "5000Tid", "2000Watt", "2000Tid", "60Watt", "liggroProsent", "liggroKg", "cmSargeant", "antBev"};
        String[] bTest = {"3000Total", "3000Tid", "2000Watt", "2000Tid", "60Watt", "kroppshev", "cmSargeant", "antBev"};
        String[] bTest2020 = {"3000Total", "3000Min", "3000Sek", "2000Watt", "2000Tid", "60Watt", "kroppshev", "cmSargeant", "antBev"};
        String[] cTest = {"3000m", "60roergo", "kroppshev", "Beveg"};

        //Add the first keys that are the same for all the classes except the C classes.
        //The C classes's keys are added in another order.
        if (allClasses_C_Excluded) {
            keys.add("rank");
            keys.add("score");
            keys.add("født");
            keys.add("navn");
            keys.add("klubb");
        } else if (c) {
            keys.add("navn");
            keys.add("født");
            keys.add("klubb");
        }

        //Adding the Array that matches the boolean expresion to the key ArrayList.
        if (senior) {
            keys.addAll(Arrays.asList(seniorTest));
        } else if (a) {
            keys.addAll(Arrays.asList(aTest));
        } else if (b2020) {
            keys.addAll(Arrays.asList(bTest2020));
        } else if (b) {
            keys.addAll(Arrays.asList(bTest));
        } else if (c) {
            keys.addAll(Arrays.asList(cTest));
        }

        return keys;
    }
}
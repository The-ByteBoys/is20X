package tools.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.IOException;
import java.util.*;


//This class reads from the excels files located in the src/main/java/tool/excel/exelDocuments/.
//Please move your xlsx files to this directory.

public class ExcelReader {

    XSSFSheet sheet;
    ArrayList<File> excelDocuments;
    ArrayList<XSSFWorkbook> workbooks;
    int row;

    public ExcelReader(int wbNr, int sheetNr){
        chooseDocument(wbNr, sheetNr);
    }

    public static void main(String[] args) {
        ExcelReader er = new ExcelReader(10, 0);
        er.getRowValues(5);
    }


    public void chooseDocument(int workbookIndex, int sheetIndex) {
        File excelDirectory = new File("src/main/java/tools/excel/excelDocuments");
        excelDocuments = new ArrayList<>(Arrays.asList(Objects.requireNonNull(excelDirectory.listFiles())));
        workbooks = new ArrayList<>();

        for (File file : excelDocuments) {
            try {
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                workbooks.add(workbook);
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }
        }

        sheet = workbooks.get(workbookIndex).getSheetAt(sheetIndex);
    }

    public void getRowValues(int rowNumber) {
        Row row = sheet.getRow(rowNumber);
        Iterator<Cell> cell = row.cellIterator();
        HashMap<String, Object> rowValues = new HashMap<String, Object>();
        ArrayList<String> keys = keyGenerator();

        int i = 0;

        while (cell.hasNext() && i < keys.size() - 1) {
            Cell c = cell.next();
                switch (c.getCellType()) {

                    case NUMERIC:
                        rowValues.put(keys.get(i), c.getNumericCellValue());
                        break;

                    case STRING:
                        rowValues.put(keys.get(i), c.getStringCellValue());
                        break;

                    case BLANK:
                        rowValues.put(keys.get(i), null);
                }
            i++;
        }

        for (Map.Entry<String, Object> set : rowValues.entrySet()) {
            System.out.println(set.getKey() + " = " + set.getValue());
        }

    }

    private ArrayList<String> keyGenerator(){
        Row row = sheet.getRow(0);
        String athleteClass = row.getCell(0).getStringCellValue();
        ArrayList<String> keys = new ArrayList<>();

        //Boolean variables that check if the A1 contains something that can ID the athlete class.
        boolean senior = athleteClass.contains("senior");
        boolean a = athleteClass.contains("A");
        boolean b = athleteClass.contains("B");
        boolean c = athleteClass.contains("C");
        boolean allClasses_C_Excluded = senior || a || b;


        //TODO: the tests for the two b classes have some differences in the 2020 rapports.
        //Arrays that contains keys for each class
        String[] seniorTest = {"5000Watt", "5000Tid", "2000Watt", "2000Tid", "60Watt", "liggroProsent", "liggroKg", "knebProsent", "knebKg", "antBev"};
        String[] aTest = {"5000Watt", "5000Tid", "2000Watt", "2000Tid", "60Watt", "liggroProsent", "liggroKg", "cmSargeant", "antBev"};
        String[] bTest = {};
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
        } else if (b) {
            keys.addAll(Arrays.asList(bTest));
        } else if (c) {
            keys.addAll(Arrays.asList(cTest));
        }

        return keys;
    }
}

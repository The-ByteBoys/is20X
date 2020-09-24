package tools.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import tools.DbTool;
import java.lang.Math;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.*;


//This class reads from the excels files located in the src/main/java/tool/excel/exelDocuments/.
//Please move your xlsx files to this directory.

public class ReadExcel {

    FileInputStream file;
    String path;
    Scanner s;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    ArrayList<File> excelDocuments;
    ArrayList<XSSFWorkbook> workbooks;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Choose a workbook (0 - 10)");
        int chosenWB = s.nextInt();
        System.out.println("Choose a sheet number (0 - 7");
        int chosenSheet = s.nextInt();

        ReadExcel er = new ReadExcel();
        er.chooseDocument(chosenWB, chosenSheet);
        er.readExcelSheet();
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

    //printer ut all
    public void readExcelSheet() {
        try {
            //Iterate through each rows one by one
            for (Row row : sheet) {
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            double i = Math.round(cell.getNumericCellValue());
                            System.out.print(i + "\t\t ");
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t ");
                            break;

                    }
                }
                System.out.println("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

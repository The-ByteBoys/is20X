package tools.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Scanner;


public class ReadExcel {
    public static void main(String[] args) {
        try {
            Scanner s = new Scanner(System.in);

            System.out.println("Give me the absolute path to an Excel sheet");
            String path = s.nextLine();
            FileInputStream file = new FileInputStream(new File(path));

            //Create workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(1);

            //Iterate through each rows one by one
            for (Row row : sheet) {
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + " ");
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue() + " ");
                            break;

                    }
                }
                System.out.println("");
            }
            file.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

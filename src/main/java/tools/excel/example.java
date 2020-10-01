package tools.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class example {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        ExcelReader er = new ExcelReader();
        er.chooseDocument("src/main/java/tools/excel/excelDocuments/2016_11.xlsx");
        er.getSheet(1);
        HashMap<String, Object> mylist = er.getRowValues(4);

        for (Map.Entry<String, Object> set : mylist.entrySet()) {
            System.out.println(set.getKey() + " = " + set.getValue());
        }

    }

}

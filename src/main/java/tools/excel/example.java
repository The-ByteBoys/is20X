package tools.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class example {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        ExcelReader er = new ExcelReader();
        er.chooseDocument("src/main/java/tools/excel/excelDocuments/2020_11.xlsx");
        er.getSheet(7);
        HashMap<String, Object> myList = er.getRowValues(1);

        for (Map.Entry<String, Object> set : myList.entrySet()) {
            System.out.println(set.getKey() + " = " + set.getValue());
        }

        er.closeWb();

    }

}

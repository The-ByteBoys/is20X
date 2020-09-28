package tools.excel;

import java.util.HashMap;
import java.util.Map;

public class example {
    public static void main(String[] args) {

        ExcelReader er = new ExcelReader();
        er.chooseDocument(5,5);
        HashMap<String, Object> mylist = er.getRowValues(4);

        for (Map.Entry<String, Object> set : mylist.entrySet()) {
            System.out.println(set.getKey() + " = " + set.getValue());
        }
    }
}

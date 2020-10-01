How to use the ExcelReader.
1. Create a field that holds a new ExcelReader object. Feks: ExcelReader er = new ExcelReader();
2. Call chooseDocument(path. Give the path of a xlsx file as the parameter. It will throw exceptions if the path is wrong or or its not a xlsx file.
3. Call getSheet(SheetIndex). There are 8 classes. Therefor the sheetIndex range from 0 included to 7 included. Will throw NullPointer exception if chooseDocumument() isnt called or the sheet is out fo bounds.
4. Create a new field that holds the HashMap<String, Object> witch returns when calling getRowValues(rowIndex). Rank 1 starts at rowIndex 0;

Then you can get the values by their key from the HashMap. The keys can be found in the keyGenerator() function.

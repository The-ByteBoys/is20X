How to use the ExcelReader.
1. Create a field that holds a new ExcelReader object. Feks: ExcelReader er = new ExcelReader();
2. Call chooseDocument(workBookIndex, sheetIndex). Both parameters start at 0. Feks: er.chooseDocument(10,0);
3. Create a new field that holds the HashMap<String, Object> witch returns when calling getRowValues(rowIndex). Rank 1 starts at rowIndex 0;

Then you can get the values by their key from the HashMap. The keys can be found in the keyGenerator() function.

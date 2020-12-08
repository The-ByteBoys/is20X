package tools.htmltools;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Eirik Svagård
 */
public class HtmlTableUtil {

    private StringBuilder html;
    private ArrayList<String> tableHeaders;
    private ArrayList< ArrayList<String> > tableRows;
    private String editCell;
    private String tableID;
    private ArrayList<String> newCellRow;
    private String tableCaption;

    public HtmlTableUtil(HashMap<String, Object> inputs){
        Iterator iterator = inputs.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry next = (Map.Entry) iterator.next();
            addHeader(next.getKey().toString());
        }
    }

    public HtmlTableUtil(String... headers){
        tableHeaders = new ArrayList<>();
        tableRows = new ArrayList<>();

        tableHeaders.addAll(Arrays.asList(headers));
    }

    public void addHeader(String newHeader){
        if(!tableHeaders.contains(newHeader)){
            tableHeaders.add(newHeader);
        }
    }

    public void setTableID(String newTableID){
        tableID = newTableID;
    }

    public void setTableCaption(String tableCaption) {
        this.tableCaption = tableCaption;
    }

    public void addHeaders(Set<String> headerName){
        tableHeaders.addAll(headerName);
    }

    public void addRow(String... inputs){
        ArrayList<String> newRow = new ArrayList<>();
        newRow.addAll(Arrays.asList(inputs));

        tableRows.add(newRow);
    }
    public void addRow(ArrayList<String> newRowList){
        tableRows.add(newRowList);
    }

    public void addCell(String cellText){
        if(newCellRow == null){
            newRow();
        }
        newCellRow.add(cellText);
    }

    public void setCell(int colIndex, String cellText){
        if(newCellRow == null){
            newRow();
        }
        colIndex = colIndex-1;

        if(newCellRow.size() <= colIndex){
            for (int i = newCellRow.size(); i < tableHeaders.size(); i++) {
                newCellRow.add("");
            }
        }
        newCellRow.set(colIndex, cellText);
    }

    public void newRow(){
        if(newCellRow != null && !newCellRow.isEmpty()){
            addRow(newCellRow);
        }
        newCellRow = new ArrayList<>();
    }

    public void addRow(Collection<Object> inputs){
        ArrayList<String> newRow = new ArrayList<>();
        for(Object input : inputs){
            newRow.add(input.toString());
        }

        tableRows.add(newRow);
    }

    public void addEditCell(String editCellHtml){
        editCell = editCellHtml;
    }

    @Override
    public String toString(){
        StringBuilder toReturn = new StringBuilder();

        toReturn.append("<table ").append(tableID != null ? "id='" + tableID + "' " : "").append("class='table table-striped table-bordered'>\n");
        if(tableCaption != null){
            toReturn.append("  <caption>").append(tableCaption).append("</caption>\n");
        }
        toReturn.append("  <thead><tr><th>");
        toReturn.append(String.join("</th><th>", tableHeaders));
        toReturn.append("</th></tr></thead>\n");

        toReturn.append("  <tbody>\n");

        int tableRow = 0;
        for(ArrayList<String> row : tableRows){
            tableRow++;

            toReturn.append("    <tr class='row").append(tableRow).append("'>");
            toReturn.append("<td>").append(String.join("</td><td>", row)).append("</td>");
            if(editCell != null){
                toReturn.append("<td>").append(editCell).append("</td>");
            }
            toReturn.append("</tr>\n");
        }


        toReturn.append("  </tbody>\n");
        toReturn.append("</table>");

        return toReturn.toString();
    }

}

package tools;

import java.util.*;

/**
 *
 * @author Eirik Svagård
 */
public class HtmlTableUtil {

    private StringBuilder html;
    private ArrayList<String> tableHeader;
    private ArrayList< ArrayList<String> > tableRows;
    private String editCell;


    public HtmlTableUtil(){

    }

    public HtmlTableUtil(HashMap<String, Object> inputs){
        Iterator iterator = inputs.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry next = (Map.Entry) iterator.next();
            addHeader(next.getKey().toString());
        }
    }

    public HtmlTableUtil(String... headers){
        tableHeader = new ArrayList<>();
        tableRows = new ArrayList<>();

        tableHeader.addAll(Arrays.asList(headers));
    }

    public void addHeader(String newHeader){
        if(!tableHeader.contains(newHeader)){
            tableHeader.add(newHeader);
        }
    }

    public void addHeaders(Set<String> headerName){
        tableHeader.addAll(headerName);
    }

    public void addRow(String... inputs){
        ArrayList<String> newRow = new ArrayList<>();
        newRow.addAll(Arrays.asList(inputs));

        tableRows.add(newRow);
    }
    public void addRow(ArrayList<String> newRowList){
        tableRows.add(newRowList);
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
        toReturn.append("<table class='table table-striped table-bordered'>");
        toReturn.append("<thead><tr><th>");
        toReturn.append(String.join("</th><th>",tableHeader));
        toReturn.append("</th></tr></thead>");

        toReturn.append("<tbody>");

        for(ArrayList<String> row : tableRows){
            toReturn.append("<tr>");
            toReturn.append("<td>"+String.join("</td><td>", row)+"</td>");
            if(editCell != null){
                toReturn.append("<td>"+editCell+"</td>");
            }
            toReturn.append("</tr>");
        }


        toReturn.append("</tbody>");
        toReturn.append("</table>");

        return toReturn.toString();
    }

}

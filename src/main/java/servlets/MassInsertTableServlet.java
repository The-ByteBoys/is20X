package servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import tools.HtmlTableUtil;
import tools.excel.ExcelReader;
import tools.html.htmlConstants;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Eirik Svagård
 */
@WebServlet(name= "MassInsert", urlPatterns = {"/massinsert"})
public class MassInsertTableServlet extends AbstractAppServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponseHeadless(request, response);
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {

        out.print(htmlConstants.getHtmlHead("Sett inn data - Roing Webapp"));
        out.print("<div class=\"container-fluid\" style=\"text-align: left; overflow-x: auto; padding-bottom: 20px;\" id='tableHolder'>");

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Parse the request
        List<FileItem> items = null;
        try {
            items = upload.parseRequest(req);
        } catch (FileUploadException e) {
//            e.printStackTrace();
//            return;
        }

        String cssStyle =
                "input[type=\"text\"] {\n" +
                        "    width: 80px;\n" +
                        "}\n" +
                        "input.longInput {\n" +
                        "    width: 150px;\n" +
                        "}\n" +
                        "input[type=\"text\"]:invalid {\n" +
                        "    " +
                        "color: #ff0000;\n" +
                        "    " +
                        "font-weight: bold;\n" +
                        "}\n" +
                        "td, th {\n" +
                        "    text-align: center;\n" +
                        "}\n";

        out.print("<style>" + cssStyle + "</style><script src='js/parseExcel.js'></script>");

        if (items != null){
            // Process the uploaded items
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    processUploadedFile(item, out);
                }
            }
        }

        out.print("<a href='uploadExcel.jsp'>Du kan også laste opp en fil</a>");
        out.print("</div>");

        out.print("<div class='container-sm' style='max-width: 500px; " +
                "margin-top: 20px;'>\n" +
                "    " +
                "<form id='addNewTable' action='#' class='form-group'>\n" +
                "    " +
                "<!--" +
                "    <input type='text' id='tableName' class='form-control'>-->\n" +
                "        <select class='form-control'>\n" +
                "            <option value='senior'>Senior</option>\n" +
                "    " +
                "        <option value='jA'>Junior A</option>\n" +
                "    " +
                "        <option value='jB'>Junior B</option>\n" +
                "    " +
                "        <option value='jC'>Junior C</option>\n" +
                "        " +
                "</select>\n" +
                "        <input type='submit' class='form-control' value='Add new table'>\n" +
                "    " +
                "</form>\n" +
                "</div>");
    }

    private void processUploadedFile(FileItem item, PrintWriter out){

        if(item.getName().contains(".xlsx")){
            try {
                // Initiate download location
                File uploadedFile = new File("/opt/payara/excel/tempfile");

                // Delete file if it exists
                if(uploadedFile.exists()){ uploadedFile.delete(); }

                // Write to the file
                item.write(uploadedFile);
            } catch (Exception e) {
                e.printStackTrace();
                out.print("<br>Failed to write file: "+e);
                return;
            }

            try {
                ExcelReader er = new ExcelReader();
                er.chooseDocument("/opt/payara/excel/tempfile");

                out.print("<h2>Reading from file: "+item.getName()+"</h2>\n" +
                        "<p>\n" +
                        "    <label>År: <input onchange='$(\".yearPicker\").val( this.value );' type='number' name='year' min='1980' max='2100' value='"+ Calendar.getInstance().get(Calendar.YEAR)+"' style='width: 70px; display: initial;' class='form-control' /></label>\n" +
                        "    <select onchange='$(\".weekPicker\").val( this.value );' name='week' style='width: initial; display: initial;' class='form-control'>\n" +
                        "        <option>Velg uke</option>\n" +
                        "        <option>2</option>\n" +
                        "        <option>11</option>\n" +
                        "        <option>44</option>\n" +
                        "    </select>\n" +
                        "</p>");
                parseExcel(er, out);

                er.closeWb();
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }

        }
        else {
            out.print("Unexpected filetype. Expected xlsx");
        }

    }

    private void parseExcel(ExcelReader er, PrintWriter out){

        int numSheets = er.getNumberOfSheets();
        if(numSheets <= 0){
            return;
        }

        String sheetName;
        HtmlTableUtil htmlTable;
        int rowNum;

        for (int i = 0; i < numSheets; i++) {
            er.getSheet(i);

            sheetName = er.getSheetName(i);
            if(
                    !sheetName.toLowerCase().contains("senior") &&
                    !sheetName.toLowerCase().contains("jun") &&
                    !sheetName.toLowerCase().contains("gutter") &&
                    !sheetName.toLowerCase().contains("jenter")
            ){
                out.print("<p>Sheet '"+sheetName+"' might be without content</p><hr>\n");
                continue;
            }
            out.print("<h3>Ark: "+sheetName+" [<span style='font-weight: normal; text-decoration: underline;' onclick='$(\"#table"+(i+1)+"\").slideToggle(200);'>Toggle view</span>]</h3>");
            out.print("<div id='table"+(i+1)+"'>");

            htmlTable = new HtmlTableUtil("Fornavn", "Etternavn", "Fødselsår", "Klubb");

            htmlTable.addEditCell("<img src='add.png' onClick='addNewRow($(this).parent().parent());' style='cursor: pointer;' alt='+' />&nbsp;<img src='remove.png' onClick='deleteRow($(this).parent().parent());' style='cursor: pointer;' alt='-' />");

            rowNum = 0;
            while(true) {
                HashMap<String, Object> mylist;
                try {
                    mylist = er.getRowValues(rowNum);
                }
                catch (NullPointerException e){
                    e.printStackTrace();
                    break;
                }

                if (mylist.get("navn") == null) {
                    break;
                }
                ArrayList<String> newRow = new ArrayList<>();

                String newLastName;
                String newFirstName;

                // If name is in form Lastname, Firstname; set it accordingly.
                if(mylist.get("navn").toString().contains(",")) {
                    String[] newNames = mylist.get("navn").toString().split(",");
                    newLastName = newNames[0].trim();
                    newFirstName = newNames[1].trim();
                }
                else {
                    // Get the name-field from dataset and put each name in a String[]. TRIM to get rid of spaces at the end or beginning of cells
                    String[] newNames = mylist.get("navn").toString().trim().split(" ");

                    newLastName = newNames[newNames.length - 1];

                    newFirstName = String.join(" ", newNames).replace(" " + newLastName, "");
                }

                newRow.add(insertFormElement("fname", newFirstName, "longInput"));
                newRow.add(insertFormElement("lname", newLastName, "longInput", "required"));

                int newBirth = 0;
                if (mylist.get("født") != null) {
                    try {
                        newBirth = (int) ((double) mylist.get("født"));
                    } catch (Exception ignored) {
                    }
                }
                if(sheetName.toLowerCase().contains("senior")){
                    newRow.add(insertFormElement("birth", String.valueOf(newBirth)));
                }
                else {
                    newRow.add(insertFormElement("birth", String.valueOf(newBirth), "", "pattern=\"[0-9]{4}\""));
                }


                String newClubs = mylist.get("klubb").toString().trim();
                newRow.add(insertFormElement("clubs", newClubs));

                ArrayList<String> currentKeys = er.keyGenerator();
                for (String key : currentKeys) {
                    if(!key.equals("navn") && !key.equals("født") && !key.equals("klubb") && !key.equals("rank") && !key.equals("score")){ //  && (!key.equals("3000Total") && mylist.containsKey("3000Tid"))
                        if(rowNum == 0){
                            htmlTable.addHeader(beautifyTableHeader(key));
                        }

                        String numberFormatPattern = "pattern=\"[0-9-]+(\\.[0-9]*)?\"";
                        String timeFormatPattern = "pattern=\"[0-9]+:[0-9]{1,2}(\\.[0-9]*)?\"";

                        if(mylist.get(key) != null){
                            if(key.equals("3000Tid") && mylist.get("3000Total") != null){

                                try {
                                    double totalSecs = Double.parseDouble(mylist.get("3000Total").toString());
                                    double totalMinutes = totalSecs/60;
                                    int minutes = (int) Math.floor(totalMinutes);
                                    double secounds = ((totalMinutes-minutes)*60);

                                    DecimalFormat df = new DecimalFormat("00.##");
                                    String timeString = minutes+":"+df.format(secounds);
                                    newRow.add(insertFormElement(key, timeString, "", timeFormatPattern));
                                }
                                catch (NumberFormatException e){
                                    newRow.add(insertFormElement(key, "math failed", "failed", timeFormatPattern));
                                }
                            }
                            else if(key.equals("5000Tid") || key.equals("2000Tid")){

                                String timeString = mylist.get(key).toString().trim();

                                if(timeString.matches("[0-9]+:[0-9]{1,2}(\\.[0-9]*)?")){
                                    // Perfect
                                }
                                else if(timeString.matches("[0-9]+[.,:;]+[0-9]{1,2}([.,;:]+[0-9]*)?")){
                                    timeString = timeString.replaceFirst("[.,:;]+","F");
                                    timeString = timeString.replaceFirst("[.,:;]+","S");
                                    timeString = timeString.replace("F",":");
                                    timeString = timeString.replace("S",".");
                                }
                                else if(timeString.matches("[0-9]+.0[0-9]+")){

                                    double newTime = Double.parseDouble(timeString);
                                    double newMinutes = newTime*24*60;
                                    double newSeconds = (newMinutes-Math.floor(newMinutes))*60;

                                    DecimalFormat df = new DecimalFormat("00.##");
                                    timeString = (int) Math.floor(newMinutes)+":"+df.format(newSeconds);
                                }


                                // Calculate time from watts for verification of this value
                                String checkValueTxt = "";
                                if(mylist.containsKey( key.replace("Tid", "Watt") )){
                                    checkValueTxt = " "+wattsToTimeStr(key.replace("Tid", ""), mylist.get(key.replace("Tid", "Watt")).toString());
                                }

                                newRow.add(insertFormElement(key, timeString, "", timeFormatPattern +checkValueTxt));
                            }
                            else {
                                newRow.add(insertFormElement(key, mylist.get(key).toString(), "", numberFormatPattern));
                            }
                        }
                        else {
                            newRow.add(insertFormElement(key, "", "", numberFormatPattern));
                        }
                    }

                }

                htmlTable.addRow(newRow);
                rowNum++;
            }

            out.print("<form method='post' id='tableForm"+(i+1)+"' action='postExcel' target='_blank'>");
            out.print("<div class=\"\">");

            out.print(htmlTable);
            out.print("<select class='sexPicker form-control' name=\"sex\" style='width: initial; display: initial;'> " +
                    "<option value='-'>Velg kjønn</option> " +
                    "<option value='M'>Menn</option> " +
                    "<option value='F'>Kvinner</option> " +
                    "<option value='O'>Andre</option> " +
                    "</select>");
            out.print("<label>År: <input class='yearPicker form-control' type='number' name='year' min='1980' max='2100' value='"+Calendar.getInstance().get(Calendar.YEAR)+"' style='width: 70px; display: initial;' /></label>");
            out.print("<select class='weekPicker form-control' name='week' style='width: initial; display: initial;'>\n" +
                    "    <option>Velg uke</option>\n" +
                    "    <option>2</option>\n" +
                    "    <option>11</option>\n" +
                    "    <option>44</option>\n" +
                    "</select>");
            out.print("<input type='submit' value='Submit all' class='form-control' style='width: initial; display: initial;' onclick='if($(\"#tableForm"+(i+1)+" input:invalid\").length == 0){ $(\"#table"+(i+1)+"\").slideUp(); }'>");
            out.print("</div>");
            out.print("</form>");
//            out.print("<p>* Tids-feltene er kalkulert ut fra watt-feltet.</p>");
            out.print("<p>* 3000 Tid er regnet ut fra 3000 Total i excel.</p></div><hr>");
            out.print("<script>\n" +
                    "    " +
                    "validateTable(\"table"+(i+1)+"\");\n" +
                    "    " +
                    "$(\"#table"+(i+1)+" input\").on('change', function(){\n" +
                    "        validateTable(\"table"+(i+1)+"\");\n" +
                    "    " +
                    "});\n" +
                    "</script>");
        }
    }

    private String wattsToTimeStr(String distanceStr, String value){
        try {
            int distance = Integer.parseInt(distanceStr);

            double pace = Math.pow(2.80 / Double.parseDouble(value), 1.0/3.0);
            double time = pace*distance;
            double totalMinutes = time/60;
            int minutes = (int) Math.floor(totalMinutes);
            double secounds = ((totalMinutes-minutes)*60);

            DecimalFormat df = new DecimalFormat("00.##");
            return minutes+":"+df.format(secounds);
        }
        catch (NumberFormatException e){
            return value;
        }
    }

    public String insertFormElement(String fieldName, String value){
        return insertFormElement(fieldName, value, "", "");
    }

    public String insertFormElement(String fieldName, String value, String cssClass){
        return insertFormElement(fieldName, value, cssClass, "");
    }

    public String insertFormElement(String fieldName, String value, String cssClass, String extra){
        String toReturn = "<input type='text' name='"+fieldName+"' value='"+value+"' class='form-control";
        if(!cssClass.isEmpty()){
            toReturn += "  "+cssClass+"";
        }
        toReturn += "'";
        if(!extra.isEmpty()){
            toReturn += " "+extra;
        }

        toReturn += " />";

        return toReturn;
    }

    private String beautifyTableHeader(String input){
        switch (input) {
            case "5000Watt":
                return "5000 Watt";
            case "5000Tid":
                return "5000 Tid";
            case "3000Total":
                return "3000 Total";
            case "3000Tid":
                return "3000 Tid*";
            case "2000Watt":
                return "2000 Watt";
            case "2000Tid":
                return "2000 Tid";
            case "60Watt":
                return "60 Watt";
            case "liggroProsent":
                return "Liggende rotak %";
            case "liggroKg":
                return "Liggende rotak KG";
            case "kroppshev":
                return "Kroppshevinger";
            case "knebProsent":
                return "Knebøy %";
            case "knebKg":
                return "Knebøy KG";
            case "cmSargeant":
                return "Sargeant CM";
            case "antBev":
                return "Antall Bevegelser";
            default:
                return input;
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

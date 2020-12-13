package servlets;

import enums.UserLevel;
import models.UserModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import tools.htmltools.HtmlTableUtil;
import tools.UserAuth;
import tools.excel.ExcelReader;
import tools.htmltools.HtmlConstants;

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
        UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);

        writeResponseHeadless(request, response, currentUser);
    }


    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {

        out.print(HtmlConstants.getHtmlHead("Sett inn data - Roing Webapp", currentUser));
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
            e.printStackTrace();
        }

        String cssStyle =
                "input[type=\"text\"] {\n" +
                        "    width: 80px;\n" +
                        "}\n" +
                        "input.longInput {\n" +
                        "    width: 150px;\n" +
                        "}\n" +
                        "input[type=text]:invalid, input[type=date]:invalid {\n" +
                        "    color: #ff0000;\n" +
                        "    font-weight: bold;\n" +
                        "}\n" +
                        "td, th {\n" +
                        "    text-align: center;\n" +
                        "}\n" +
                        "\n" +
                        "[data-theme=\"dark\"] .svgIcon {\n" +
                        "    filter: invert(80%);\n" +
                        "}";

        out.print("<style>" + cssStyle + "</style><script src='js/parseExcel.js'></script>");

        if (items != null){
            // Process the uploaded items
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    processUploadedFile(item, out);
                }
            }
        }

        out.print("<p style='text-align: center;'><a href='uploadExcel.jsp'>Du kan også laste opp en fil</a></p>");
        out.print("</div>");

        out.print("<div class='container-sm' style='max-width: 500px; " +
                "margin-top: 20px;'>\n" +
                "    " +
                "<form id='addNewTable' action='#' class='form-group'>\n" +
                "    " +
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


    /**
     * Process uploaded file
     * @param item item from uploaded POST file
     * @param out  HTML output for errors
     */
    private void processUploadedFile(FileItem item, PrintWriter out){

        if(item.getName().contains(".xlsx")){
            try {
                File uploadedFile = new File("/opt/payara/excel/tempfile");

                if(uploadedFile.exists()){ uploadedFile.delete(); }

                item.write(uploadedFile);
            } catch (Exception e) {
                e.printStackTrace();
                out.print("<br>Failed to write file: "+e);
                return;
            }

            try {
                ExcelReader er = new ExcelReader();
                er.chooseDocument("/opt/payara/excel/tempfile");
                er.setFileName(item.getName());

                out.print("<h2>Leser fra fil: "+item.getName()+"</h2>\n" +
                        "<p>\n" +
                        "    <label>År: <input onchange='$(\".yearPicker\").val( this.value ).parent().hide();' type='number' name='year' min='1980' max='2100' value='"+ Calendar.getInstance().get(Calendar.YEAR)+"' style='width: 70px; display: initial;' class='form-control' /></label>\n" +
                        "    <select onchange='$(\".weekPicker\").val( this.value ).hide();' name='week' style='width: initial; display: initial;' class='form-control'>\n" +
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


    /**
     * Parse excel-file
     * @param er  object for the excel-files reader
     * @param out servlet out for HTML
     */
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
                    !sheetName.toLowerCase().contains("jenter") &&
                    !sheetName.toLowerCase().matches("[h|d]j[a-c]")
            ){
                out.print("<p>Arket '"+sheetName+"' kan være tomt, eller støtte mangler.</p><hr>\n");
                continue;
            }
            out.print("<h3>Ark: "+sheetName+" [<span style='font-weight: normal; text-decoration: underline;' onclick='$(\"#table"+(i+1)+"\").slideToggle(200);'>Skjul / vis</span>]</h3>");
            out.print("<div id='table"+(i+1)+"'>");

            htmlTable = new HtmlTableUtil("", "Fornavn", "Etternavn", "Fødselsår", "Klubb");

            htmlTable.addEditCell("<img src='img/add.svg' onClick='addNewRow($(this).parent().parent());' style='cursor: pointer; height: 21px;' alt='+' class='svgIcon' /><img src='img/remove.svg' onClick='deleteRow($(this).parent().parent());' style='cursor: pointer; height: 21px;' alt='-' class='svgIcon' />");

            rowNum = 0;
            if(sheetName.toLowerCase().contains("jun c") || sheetName.toLowerCase().matches("[h|d]jc")){
                rowNum = -1;
            }
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

                newRow.add("<img src='img/search.svg' style='cursor: pointer; height: 21px;' alt='se' class='searchBtn svgIcon' />");

                newRow.add(insertFormElement("fname", newFirstName, "longInput"));
                newRow.add(insertFormElement("lname", newLastName, "longInput", "required"));

                int newBirth = 0;
                if (mylist.get("født") != null) {
                    try {
                        newBirth = (int) ((double) mylist.get("født"));
                    } catch (Exception ignored) {
                    }
                }
                newRow.add("<input type='date' name='birth' value='"+newBirth+"-01-01' class='form-control'"+(!sheetName.toLowerCase().contains("senior")?" required":"")+">");


                String newClubs = mylist.get("klubb").toString().trim();
                newRow.add(insertFormElement("clubs", newClubs));

                ArrayList<String> currentKeys = er.keyGenerator();
                for (String key : currentKeys) {
                    if(!key.equals("navn") && !key.equals("født") && !key.equals("klubb") && !key.equals("rank") && !key.equals("score") && !key.equals("3000Tid") && !(mylist.containsKey("3000Total") && key.equals("3000Min")) && !key.equals("3000Sek")){
                        if(rowNum == 0){
                            htmlTable.addHeader(beautifyTableHeader(key));
                        }

                        String numberFormatPattern = "pattern=\"[0-9-]+(\\.[0-9]*)?\"";
                        String timeFormatPattern = "pattern=\"[0-9]+:[0-9]{1,2}(\\.[0-9]*)?\"";

                        if(mylist.get(key) != null){

                            switch (key) {
                                case "3000Min":
                                    // CASE OF REPORT 2020, week 44
                                    newRow.add(insertFormElement("3000Total", (int) Double.parseDouble(mylist.get("3000Min").toString()) + ":" + mylist.get("3000Sek"), "", timeFormatPattern));
                                    break;

                                case "3000Total":
                                    try {
                                        double totalSecs = Double.parseDouble(mylist.get(key).toString());
                                        double totalMinutes = totalSecs / 60;
                                        int minutes = (int) Math.floor(totalMinutes);
                                        double secounds = ((totalMinutes - minutes) * 60);

                                        DecimalFormat df = new DecimalFormat("00.##");
                                        String timeString = minutes + ":" + df.format(secounds);
                                        newRow.add(insertFormElement(key, timeString, "", timeFormatPattern));
                                    } catch (NumberFormatException e) {
                                        newRow.add(insertFormElement(key, "math failed", "failed", timeFormatPattern));
                                    }
                                    break;

                                case "5000Tid":
                                case "2000Tid":
                                case "3000m":
                                    String timeString = mylist.get(key).toString().trim();

                                    if (timeString.matches("[0-9]+:[0-9]{1,2}(\\.[0-9]*)?")) {
                                        // Perfect
                                    } else if (timeString.matches("[0-9]+[.,:;]+[0-9]{1,2}([.,;:]+[0-9]*)?")) {
                                        timeString = timeString.replaceFirst("[.,:;]+", "F");
                                        timeString = timeString.replaceFirst("[.,:;]+", "S");
                                        timeString = timeString.replace("F", ":");
                                        timeString = timeString.replace("S", ".");
                                    } else if (timeString.matches("[0-9]+.[0-9]+")) {
                                        double newTime = Double.parseDouble(timeString);
                                        double newMinutes;

                                        if (newTime >= 0.1) {
                                            // newMinutes is calculated as newHours.
                                            newMinutes = newTime * 24; // (hours)
                                        } else {
                                            // Converts the time from days to minutes (e.g: 0.01255787037)
                                            newMinutes = newTime * 24 * 60;
                                        }

                                        double newSeconds = (newMinutes - Math.floor(newMinutes)) * 60;

                                        DecimalFormat df = new DecimalFormat("00.##");
                                        timeString = (int) Math.floor(newMinutes) + ":" + df.format(newSeconds);
                                    }


                                    // Calculate time from watts for verification of this value
                                    String checkValueTxt = "";
                                    if (mylist.get(key.replace("Tid", "Watt")) != null && !key.equals("3000m")) {
                                        checkValueTxt = " " + wattsToTimeStr(key.replace("Tid", ""), mylist.get(key.replace("Tid", "Watt")).toString());
                                    }

                                    newRow.add(insertFormElement(key, timeString, "", timeFormatPattern) + checkValueTxt);
                                    break;

                                default:
                                    newRow.add(insertFormElement(key, mylist.get(key).toString().replace(",", "."), "", numberFormatPattern));
                                    break;
                            }
                        }
                        else {
                            key = key.replace("3000Min", "3000Total");
                            newRow.add(insertFormElement(key, "", "", numberFormatPattern));
                        }
                    }

                }

                htmlTable.addRow(newRow);
                rowNum++;
            }

            out.print("<form method='post' id='tableForm"+(i+1)+"' action='postExcel' target='_blank'>");
            out.print("<div class=\"\">");
            String classValue = "";
            if(sheetName.contains("Senior")){
                classValue = "senior";
            }
            else if(sheetName.contains("Jun A") || sheetName.toLowerCase().matches("[h|d]ja")){
                classValue = "junA";
            }
            else if(sheetName.contains("Jun B") || sheetName.toLowerCase().matches("[h|d]jb")){
                classValue = "junB";
            }
            else if(sheetName.contains("Jun C") || sheetName.toLowerCase().matches("[h|d]jc")){
                classValue = "junC";
            }

            out.print("<input type='hidden' name='class' value='"+classValue+"'>");

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
            out.print("</div><hr>");
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


    /**
     * Calculates time from watts
     * @param distanceStr distance the watts was made for (in string)
     * @param value       input watts value
     * @return string with the time it took in format MM:ss
     */
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


    /**
     * Generate HTML for the different input-fields used here
     * @param fieldName exercise key
     * @param value     value
     * @param cssClass  css-class (optional)
     * @param extra     extra fields to go in the input, e.g: "disabled" or "pattern='[a-z]'" (optional)
     * @return HTML for said input
     */
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


    /**
     * Return nicer text for the given key
     * @param input key
     * @return nicer text for the key
     */
    private String beautifyTableHeader(String input){
        switch (input) {
            case "5000Watt":
                return "5000 Watt";
            case "5000Tid":
                return "5000 Tid";
            case "3000Total":
                return "3000m Tid";
            case "2000Watt":
                return "2000 Watt";
            case "2000Tid":
                return "2000 Tid";
            case "60roergo":
            case "60Watt":
                return "60 Watt";
            case "liggroProsent":
                return "Liggende rotak i %";
            case "liggroKg":
                return "Liggende rotak i KG";
            case "kroppshev":
                return "Kroppshevinger";
            case "knebProsent":
                return "Knebøy i %";
            case "knebKg":
                return "Knebøy i KG";
            case "cmSargeant":
                return "Sargeant (CM)";
            case "antBev":
            case "Beveg":
                return "Antall bevegelser";
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
        return "Mass insert form(s)";
    }
}

package servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import tools.HtmlTableUtil;
import tools.excel.ExcelReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

// import models.AthleteModel;
// import tools.repository.Athletes;

/**
 *
 * @author Eirik Svagård
 */
@WebServlet(name= "ParseExcelServlet", urlPatterns = {"/parseExcel"})
public class ParseExcelServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponse(request, response, "Import Excel - Roing Webapp");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {

        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Parse the request
        List<FileItem> items;
        try {
            items = upload.parseRequest(req);
        } catch (FileUploadException e) {
//            e.printStackTrace();

            out.print("<form method=\"POST\" enctype=\"multipart/form-data\" action=\"parseExcel\">\n" +
                    "    File to upload: <input type=\"file\" name=\"upfile\"><br/>\n" +
                    "    Notes about the file: <input type=\"text\" name=\"note\"><br/>\n" +
                    "    <br/>\n" +
                    "    <input type=\"submit\" value=\"Press\"> to upload the file!\n" +
                    "</form>");
            return;
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
                        "color: red;\n" +
                        "    " +
                        "font-weight: bold;\n" +
                        "}";

        out.print("<style>"+cssStyle+"</style>");

        // Process the uploaded items
        for (FileItem item : items) {
            if (item.isFormField()) {
//                out.print("<br>Form field: " + item.getFieldName() + " - " + item.getName());
            } else {
                processUploadedFile(item, out);
            }
        }
    }

    private void processUploadedFile(FileItem item, PrintWriter out){
        out.print("File: "+item.getName()+"<br>\n");
        out.print("Content-type "+item.getContentType()+"<br>\n");

        // CHECKS
        if(item.getName().contains(".xlsx")){
            out.println("File is a xlsx document!<br>");

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

                parseExcel(er, out);

                er.closeWb();
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }

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
            out.print("<h3 onclick='document.getElementById(\"table"+i+"\").style.display = \"block\";'>Sheet: "+sheetName+"</h3>");
            out.print("<div id='table"+i+"'>");

            htmlTable = new HtmlTableUtil("Fornavn", "Etternavn", "Fødselsår", "Klubb");

            htmlTable.addEditCell("Edit");

            rowNum = 0;
            while(true) {
                HashMap<String, Object> mylist = er.getRowValues(rowNum);
                if (mylist.get("navn") == null) {
                    break;
                }
                ArrayList<String> newRow = new ArrayList<>();

                // Get the name-field from dataset and put each name in a String[]. TRIM to get rid of spaces at the end or beginning of cells
                String[] newNames = mylist.get("navn").toString().trim().split(" ");

                // Last name is the last name in string
                String newLastName = newNames[newNames.length - 1];

                // First name is the rest of the name, except the last name
                String newFirstName = String.join(" ", newNames).replace(" " + newLastName, "");

                newRow.add(insertFormElement("fname", newFirstName, "longInput"));
                newRow.add(insertFormElement("lname", newLastName, "longInput", "required"));

                int newBirth = 0;
                if (mylist.get("født") != null) {
                    try {
                        newBirth = (int) ((double) mylist.get("født"));
                    } catch (Exception ignored) {
                    }
                }
                newRow.add(insertFormElement("birth", String.valueOf(newBirth)));

                String newClubs = mylist.get("klubb").toString().trim();
                newRow.add(insertFormElement("clubs", newClubs));

                ArrayList<String> currentKeys = er.keyGenerator();
                for (String key : currentKeys) {
                    if(!key.equals("navn") && !key.equals("født") && !key.equals("klubb") && !key.equals("rank") && !key.equals("score")){
                        if(rowNum == 0){
                            htmlTable.addHeader(beautifyTableHeader(key));
                        }
                        if(mylist.get(key) != null){
//                            if(key.matches("(.)Tid")){
//                            if(key.equals("5000Watt") || key.equals("3000Watt") || key.equals("2000Watt")){
                            if(key.equals("5000Tid") || key.equals("3000Tid") || key.equals("2000Tid")){
                                /**
                                 * 391 = 2.80 / x³
                                 *
                                 * x = Math.pow(2.80 / 391, 3)
                                 */
                                int distance = Integer.parseInt(key.replace("Tid", ""));

                                double pace = Math.pow(2.80 / (double) mylist.get(key.replace("Tid", "Watt")), 1.0/3.0);
                                double time = pace*distance;
                                double totalMinutes = time/60;
                                int minutes = (int) Math.floor(totalMinutes);
                                double secounds = ((totalMinutes-minutes)*60);
//
                                DecimalFormat df = new DecimalFormat("00.##");
                                String timeString = minutes+":"+df.format(secounds);

//                                newRow.add(insertFormElement(key, mylist.get(key).toString())+"<br>"+);
                                newRow.add(insertFormElement(key, timeString, "", "pattern=\"[0-9]+:[0-9]{1,2}(\\.[0-9]*)?\""));
                            }
                            /*else if(key.equals("5000Tid") || key.equals("3000Tid") || key.equals("2000Tid")){


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
                                else if(timeString.matches("[0-9]+.[0-9]{15,}")){
                                    double newTime = Double.valueOf(timeString);
                                    double newMinutes = newTime*24*60;
                                    double newSeconds = (newMinutes-Math.floor(newMinutes))*60;

                                    DecimalFormat df = new DecimalFormat("##.##");
                                    timeString = (int) Math.floor(newMinutes)+":"+df.format(newSeconds);
                                }
                                newRow.add(insertFormElement(key, timeString, "", "pattern=\"[0-9]+:[0-9]{1,2}(\\.[0-9]*)?\""));
                            }*/
                            else {
                                newRow.add(insertFormElement(key, mylist.get(key).toString()));
                            }
                        }
                        else {
                            newRow.add(insertFormElement(key, ""));
                        }
                    }

                }

                htmlTable.addRow(newRow);
                rowNum++;
            }

            out.print("<form method='post' action='postExcel' target='_blank'>");
            out.print(htmlTable);
            out.print("<select name=\"sex\"> " +
                    "<option value='-'>Velg kjønn</option> " +
                    "<option value='M'>Menn</option> " +
                    "<option value='F'>Kvinner</option> " +
                    "<option value='O'>Andre</option> " +
                    "</select>");
            out.print("<input type='number' name='year' min='1980' max='2100' value='"+Calendar.getInstance().get(Calendar.YEAR)+"' style='width: 50px;' />");
            out.print("<select name='week'>\n" +
                    "    <option>2</option>\n" +
                    "    <option>11</option>\n" +
                    "    <option>44</option>\n" +
                    "</select>");
            out.print("<input type='submit' value='Submit all' onclick='document.getElementById(\"table"+i+"\").style.display = \"none\";'>");
            out.print("</form>");
            out.print("<p>* - Tids-feltene er kalkulert ut fra watt-feltet.</p></div><hr>");
        }

    }

    public String insertFormElement(String fieldName, String value){
        return insertFormElement(fieldName, value, "", "");
    }

    public String insertFormElement(String fieldName, String value, String cssClass){
        return insertFormElement(fieldName, value, cssClass, "");
    }

    public String insertFormElement(String fieldName, String value, String cssClass, String extra){
        String toReturn = "<input type='text' name='"+fieldName+"' value='"+value+"'";
        if(!cssClass.isEmpty()){
            toReturn += " class='"+cssClass+"'";
        }
        if(!extra.isEmpty()){
            toReturn += " "+extra;
        }

        toReturn += "/>";

        return toReturn;
    }

    private String beautifyTableHeader(String input){
        switch (input) {
            case "5000Watt":
                return "5000 Watt";
            case "5000Tid":
                return "5000 Tid*";
            case "3000Total":
                return "3000 Total";
            case "3000Tid":
                return "3000 Tid*";
            case "2000Watt":
                return "2000 Watt";
            case "2000Tid":
                return "2000 Tid*";
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

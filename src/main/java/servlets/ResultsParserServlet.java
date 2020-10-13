package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.openxml4j.exceptions.InvalidOperationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import enums.*;
import models.*;
import tools.repository.*;
import tools.excel.ExcelReader;

@WebServlet(name= "ResultsParserServlet", urlPatterns = {"/parseresults"})
public class ResultsParserServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponse(request, response, "Parse Excel Files");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {

        int numNewClubs = 0;
        int numNewAthletes = 0;

        out.print("<h1>Importing excel data</h1>");

        String file;
        int sheet;
        String sex;
        boolean dryrun = true;

        try {
            file = req.getParameter("file");
            sheet = Integer.parseInt(req.getParameter("sheet"));
            sex = req.getParameter("sex");

            // set dryrun = false if none of the above statements are not null, and dryrun == null
            if(file != null && sheet >= 0 && sex != null){
                dryrun = req.getParameter("dryrun") != null;
            }
        } catch(Exception e){
            out.print("Please use a numeric sheet-number: "+e);
            e.printStackTrace();
            return;
        }
        
        if(file == null || sheet < 0 || sex == null){
            out.print("Doing a dry-run! To insert into database please provide all arguments: file, sheet, sex. <br>\nE.G: "+req.getRequestURL()+"?file=2020-11.xlsx&sheet=0&sex=mann");
//            return;
        }


        /* Insert first document, first 'page', into database */
        ExcelReader er;
        try {
            er = new ExcelReader();
            er.chooseDocument("/opt/payara/excel/"+file);
            er.getSheet(sheet);
        }
        catch (Exception e){
            e.printStackTrace();
            out.print("Failed to read document. <br>\n"+e);
            return;
        }

        ArrayList<String> clubs = new ArrayList<>();
        try {
            List<ClubModel> clubModels = Clubs.getClubs();
            for(ClubModel c : clubModels){
                clubs.add( c.get(Club.NAME).toString() );
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            out.print("Failed to load clubs from database! Exception: "+e+"<br>\n");
        }

        out.print("<p><b>Import from <i>"+file+"</i></b></p>\n" +
                "<table>\n" +
                "    <thead>\n" +
                "        <tr>\n" +
                "            <th>Navn</th>\n" +
                "            <th>Klubb</th>\n" +
                "            <th>Født</th>\n" +
                "            <th>5000w</th>\n" +
                "            <th>2000w</th>\n" +
                "            <th>60\"</th>\n" +
                "            <th>ligg.ro</th>\n" +
                "            <th>knebøy</th>\n" +
                "        " +
                "</tr>\n" +
                "    " +
                "</thead>\n" +
                "    <tbody>\n" +
                "    ");


        /* LOOP going over rows in the excel document */
        int row = 0;
        Object firstCell = "first";
        while(!firstCell.equals("")){
            HashMap<String, Object> mylist = er.getRowValues(row);
            firstCell = mylist.get("navn");

            // Stop the loop if the first cell is empty
            if(firstCell == null){
                break;
            }

            // TRIM to get rid of spaces at the end or beginning of cells:
            String newName = mylist.get("navn").toString().trim();

            String[] newClubs = mylist.get("klubb").toString().trim().split("\\s*/\\s*");

            int newBirth = 0;
            if(mylist.get("født") != null){
                try {
                    newBirth = (int) ((double) mylist.get("født"));
                }
                catch(Exception e){
                    // e.printStackTrace();
                    out.print("<p>Failed to add: "+newName+"<br>"+mylist.toString()+"<br><pre>"+e+"</pre></p>");
                    row++;
                    // Continue to not stop execution of the other rows
                    continue;
                }
            }

            out.print("<tr>\n" +
                    "    <td>"+newName+"</td>\n" +
                    "    <td>"+String.join(", ", newClubs)+"</td>\n" +
                    "    <td>"+newBirth+"</td>\n" +
                    "    <td>"+mylist.get("5000Watt")+"</td>\n" +
                    "    <td>"+mylist.get("2000Watt")+"</td>\n" +
                    "    <td>"+mylist.get("60Watt")+"</td>\n" +
                    "    <td>"+mylist.get("liggroKg")+"</td>\n" +
                    "    <td>"+mylist.get("knebKg")+"</td>\n" +
                    "    <td>");


            // ADD CLUB if it doesn't exist
            for (String c: newClubs){
                if(!clubs.contains(c)){
                    out.print("New club");
                    if(!dryrun){
                        try {
                            Clubs.addClub(c);
                            numNewClubs++;
                        }
                        catch(SQLException e){
                            // Club is most likely already added
                        }
                    }
                }
                clubs.add(c);
            }

//            out.print("<br>");


            // ADD ATHLETE
            if(!dryrun){
                // MUST REFACTOR
                /*
                try {

                    List<ClubModel> aClubs = new ArrayList<>();

                    for(String c: newClubs) {
                        aClubs.add(Clubs.getClub(c));
                    }

                    AthleteModel newAthlete = new AthleteModel(newName, newBirth, null, sex);
                    Athletes.addAthlete(newAthlete, (int) club.get(Club.ID));

                    numNewAthletes++;
                }
                catch(SQLException e){
                    out.print("<b>Athlete most likely already exists.</b>");
                }*/

            }

//            out.println("<hr>");
            row++;
            out.print("</td>\n"+"</tr>");
        }

        out.print("</tbody>\n"+"</table>");

        er.closeWb();

        out.print("<h3>Added "+numNewClubs+" new clubs and "+numNewAthletes+" new athletes!");
    }

    /**
     * Returns a short description of the servlet.
     *
     */
    @Override
    public String getServletInfo() {
        return "Parse excel files to database";
    }
}

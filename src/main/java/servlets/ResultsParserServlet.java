package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        writeResponse(request, response, "Parse Excel Files");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {


        Integer numNewClubs = 0;
        Integer numNewAthletes = 0;

        out.print("<h1>Importing excel data</h1>");

        String file;
        int sheet;
        String sex;
        Boolean dryrun = true;

        try {
            file = req.getParameter("file");
            sheet = Integer.parseInt(req.getParameter("sheet"));
            sex = req.getParameter("sex");
            dryrun = req.getParameter("dryrun")!=null?true:false;
            // if(req.getParameter("dryrun") == null){
                // dryrun = false;
            // }
        } catch(Exception e){
            out.print("Please use a numeric sheet-number: "+e);
            e.printStackTrace();
            return;
        }
        
        if(file == null || sheet < 0 || sex == null){
            out.print("Please provide all arguments: file, sheet, sex. <br>\nE.G: "+req.getRequestURL()+"?file=2020-11.xlsx&sheet=0&sex=mann");
            return;
        }


        /** Insert first document, first 'page', into database */
        ExcelReader er = null;
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

        /** LOOP going over rows in the excel document */
        int row = 0;
        Object firstCell = "first";
        while(!firstCell.equals("")){
            HashMap<String, Object> mylist = er.getRowValues(row);
            firstCell = mylist.get("navn");

            if(firstCell == null){
                break;
            }

            // TRIM to get rid of spaces at the end or beginning of cells:
            String newName = mylist.get("navn").toString().trim();
            String newClub = mylist.get("klubb").toString().trim();

            Integer newBirth = 0;
            if(mylist.get("født") != null){
                try {
                    newBirth = (int) ((double) mylist.get("født"));
                }
                catch(Exception e){
                    // e.printStackTrace();
                    out.print("<p>Failed to add: "+newName+"<br>"+mylist.toString()+"<br><pre>"+e+"</pre></p>");
                    row++;
                    continue;
                }
            }


            out.print(newName+", "+newClub+", "+(newBirth > 0?"født: "+newBirth:""));


            // ADD CLUB if it doesn't exist
            if(!clubs.contains(newClub)){
                out.print(" - Adding club...");
                if(!dryrun){
                    try {
                        Clubs.addClub(newClub);
                        numNewClubs++;
                    }
                    catch(SQLException e){
                        // Club is most likely already added
                    }
                }
            }
            clubs.add(newClub);

            out.print("<br>");


            // ADD ATHLETE
            if(!dryrun){
                try {
                    ClubModel club = Clubs.getClub(newClub);

                    AthleteModel newAthlete = new AthleteModel(newName, newBirth, null, sex);
                    Athletes.addAthlete(newAthlete, (int) club.get(Club.ID));

                    numNewAthletes++;
                }
                catch(SQLException e){
                    out.print("<b>Athlete most likely already exists.</b>");
                }
            }

            out.println("<hr>");
            row++;
        }

        try {
            er.closeWb();
        }
        catch(IOException e){
            //fuck off
        }

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

package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;

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

        /** Insert first document, first 'page', into database */
        ExcelReader er = new ExcelReader();
        er.chooseDocument(0,0);


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
        while (!firstCell.equals("")) {
            HashMap<String, Object> mylist = er.getRowValues(row);
            firstCell = mylist.get("navn");
            if(firstCell == null){
                break;
            }
            String newName = mylist.get("navn").toString();
            String newClub = mylist.get("klubb").toString();

            Integer newBirth = 0;
            if(mylist.get("født") != null){
                newBirth = (int) ((double) mylist.get("født"));
            }


            out.print(newName+", "+newClub+", "+(newBirth > 0?"født: "+newBirth:""));


            // ADD CLUB if it doesn't exist
            if(!clubs.contains(newClub)){
                out.print(" - Adding club...");
                try {
                    Clubs.addClub(newClub);
                }
                catch(SQLException e){
                    // Club is most likely already added
                }
            }
            clubs.add(newClub);

            out.print("<br>");


            // ADD ATHLETE
            try {
                ClubModel club = Clubs.getClub(newClub);

                AthleteModel newAthlete = new AthleteModel(newName, newBirth, null, "mann");
                Athletes.addAthlete(newAthlete, (int) club.get(Club.ID));
            }
            catch(SQLException e){
                out.print("<b>Athlete most likely already exists.</b>");
            }

            out.println("<hr>");
            row++;
        }
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

package servlets;

import enums.*;
import models.*;
import tools.repository.*;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;


/**
 *
 * @author Eirik Svagård
 */
@WebServlet(name= "PostExcelServlet", urlPatterns = {"/postExcel"})
public class PostExcelServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponse(request, response, "Import Excel - Roing Webapp");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            out.print("I don't know what happened.. "+e);
            return;
        }

        String[] firstNames = req.getParameterValues("fname");
        String[] lastNames = req.getParameterValues("lname");
        String[] births = req.getParameterValues("birth");
        String[] clubs = req.getParameterValues("clubs");

        Map<String, String[]> parameters = req.getParameterMap();

        String sex = req.getParameter("sex");
        String year = req.getParameter("year");
        String week = req.getParameter("week");

        // CHECK sex FIELD
        if(!sex.equals("M") && !sex.equals("F") && !sex.equals("O")){
            out.print("Please go back and select a gender!");
            return;
        }

        // CHECK year AND week FIELDS
        if(year == null || week == null){
            out.print("Missing year and week fields!");
            return;
        }

        // CHECK input LENGTHS
        int inputLength = lastNames.length;
        if(firstNames.length != inputLength && births.length != inputLength && clubs.length != inputLength){
            out.print("Something wrong happened. List-lengths differ!");
            return;
        }

        for (int i = 0; i < lastNames.length; i++) {

            // GET ATHLETE, and add if doesnt exist
            int newAthleteId = 0;

            try {

                // ADD ATHLETE
                newAthleteId = addAthlete(firstNames[i], lastNames[i], Integer.parseInt(births[i]), sex);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                out.print("Failed to add athlete<br>\n"+throwables);

                // Skip this athlete and go to the next one
                continue;
            } catch (NamingException e) {
                e.printStackTrace();
                out.print("Failed to connect to datasource!<br>\n"+e);

                // Datasource isn't found, so stop execution of method
                return;
            }
            out.print("Athlete: "+newAthleteId);


            // CLUBS
            String[] newClubs = clubs[i].trim().split("\\s*/\\s*");
            for (String c : newClubs){
                c = c.trim();
                try {
                    int newClubId = Clubs.addClub(c);
                    Athletes.addAthleteToClub(newAthleteId, newClubId);
                    out.print(" - added to club \""+c+"\"");
                }
                catch(Exception e){
                    e.printStackTrace();
                    out.print(" - <b onclick='this.nextElementSibling.style.display = \"initial\";'>Failed to add athlete to club</b><span style='display: none'><br>"+e+"</span>\n");
                }
            }


            // Results?
            parameters.forEach((key, value) -> {
                if(key != "fname" && key != "lname" && key != "birth" && key != "clubs"){
                    // key = 5000Watt, 5000Tid, 2000Watt, 2000Tid, 60Watt, liggeroProsent, liggeroKg, osv..
                    // TODO: get exercise id

                    // Format testTime
                    Calendar cal = Calendar.getInstance();
                    cal.setWeekDate(Integer.parseInt(year), Integer.parseInt(week), Calendar.MONDAY);

                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String testTime = sdf.format(cal.getTimeInMillis());


                    // Insert to database
                    // insert into result (athlete, exercise, result, date_time, result_type) VALUES (newAthleteId, exerciseId, result, testTime, "IP");
                }
            });

            out.print("<br>\n");
        }
    }

    private int addAthlete(String fName, String lName, int birth, String sex) throws SQLException, NamingException {
        AthleteModel newAthlete = new AthleteModel(null, fName, lName, birth, sex);
        return Athletes.addAthlete(newAthlete);
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

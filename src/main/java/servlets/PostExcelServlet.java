package servlets;

import enums.*;
import models.*;
import org.springframework.dao.DuplicateKeyException;
import tools.repository.*;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
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
            out.print("I don't know what happened.. " + e);
            return;
        }

        String[] firstNames = req.getParameterValues("fname");
        String[] lastNames = req.getParameterValues("lname");
        String[] births = req.getParameterValues("birth");
        String[] clubs = req.getParameterValues("clubs");

        String sex = req.getParameter("sex");
        String year = req.getParameter("year");
        String week = req.getParameter("week");
        String atClass = req.getParameter("class");

        Map<String, String[]> parameters = req.getParameterMap();


        if(!sex.matches("[MFO]")){
            out.print("Please go back and select a gender!");
            return;
        }

        if(year == null || week == null){
            out.print("Missing year and week fields!");
            return;
        }

        int inputLength = lastNames.length;
        if(firstNames.length != inputLength && births.length != inputLength && clubs.length != inputLength){
            out.print("Something wrong happened. List-lengths differ!");
            return;
        }

        // Format testTime
        Calendar cal = Calendar.getInstance();
        cal.setWeekDate(Integer.parseInt(year), Integer.parseInt(week), Calendar.MONDAY);

        Timestamp testTime = new Timestamp(cal.getTimeInMillis());
        Date testTimeDate = new Date(cal.getTimeInMillis());

        for (int i = 0; i < lastNames.length; i++) {

            // GET ATHLETE, and add if athlete doesn't exist
            int newAthleteId = 0;
            Date birth;

            try {
                if(!births[i].equals("")){
                    birth = Date.valueOf(births[i]);
                }
                else {
                    birth = Date.valueOf("0001-01-01");
                }

                // ADD ATHLETE
                newAthleteId = addAthlete(firstNames[i], lastNames[i], birth, sex);

                // Add athlete to class
                Athletes.addAthleteToClass(newAthleteId, atClass, testTimeDate);

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
            out.print( addToClubs(newAthleteId, newClubs));

            for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
                String key = entry.getKey();

                if (!key.equals("fname") && !key.equals("lname") && !key.equals("birth") && !key.equals("clubs") && !key.equals("sex") && !key.equals("year") && !key.equals("week") && !key.equals("class")) {
                    String[] values = entry.getValue();
                    if(values[i].equals("")){
                        continue;
                    }

                    int exerciseId = getExerciseIdFromKey(key);
                    if(exerciseId <= 0){
                        out.print(" - Didn't find exercise for key: "+key+"<br>\n");
                        continue;
                    }

                    // Convert time fields to seconds
                    double newValue = 0.00;

                    if (key.matches("[0-9]+Tid") || key.equals("3000m") || key.equals("3000Total")){
                        String inputTime = values[i].trim();
                        if(inputTime.matches("[0-9]+:[0-9]{1,2}(\\.[0-9]*)?")){
                            String[] inputTimes = inputTime.split(":");

                            try {
                                int minutes = Integer.parseInt(inputTimes[0]);
                                double seconds = Double.parseDouble(inputTimes[1]);

                                newValue = minutes*60+seconds;
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else {
                            out.print(" - Time doesn't match expected format.");
                            continue;
                        }
                    }
                    else {
                        try {
                            newValue = Double.parseDouble(values[i]);
                        }
                        catch (Exception e){
                            out.print(" - <b onclick='this.nextElementSibling.style.display = \"initial\";'>Didn't understand input field (key: "+key+"): "+values[i]+"</b><span style='display: none'><br>"+e+"<br></span>\n");
                        }
                    }


                    try {
                        ResultModel newResult = new ResultModel(newAthleteId, exerciseId, newValue, testTime, "IP");

                        Results.addResult(newResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.print(" - <b onclick='this.nextElementSibling.style.display = \"initial\";'>Failed to add result for exercise: "+key+"</b><span style='display: none'><br>"+e+"<br></span>\n");
                    }
                }
            }

            out.print("<br>\n");
        }

        out.println("<br><strong>Success!</strong>");
    }


    /**
     * Add athlete from fields, and return their athleteID
     * @param fName first name
     * @param lName last name
     * @param birth birth-date
     * @param sex   male/female/other
     * @return athleteID
     * @throws SQLException if SQL failed
     * @throws NamingException if DataSource isn't found
     */
    private int addAthlete(String fName, String lName, Date birth, String sex) throws SQLException, NamingException {
        AthleteModel newAthlete = new AthleteModel(null, fName, lName, birth, sex);
        return Athletes.addAthlete(newAthlete);
    }


    /**
     * Add an Athlete to a club (if it isn't already in the club)
     * @param athleteId athletes ID
     * @param newClubs  array of clubs names
     * @return result of operation in text
     */
    private String addToClubs(int athleteId, String[] newClubs){
        StringBuilder toReturn = new StringBuilder();
        for (String c : newClubs){
            c = c.trim();
            try {
                int newClubId = Clubs.addClub(c);
                Athletes.addAthleteToClub(athleteId, newClubId);
                toReturn.append(" - added to club \"").append(c).append("\"");
            }
            catch(DuplicateKeyException ignore){
                // Duplicate Keys are expected.
                toReturn.append(" - already in club \"").append(c).append("\"");
            }
            catch(Exception e){
                e.printStackTrace();
                toReturn.append(" - <b onclick='this.nextElementSibling.style.display = \"initial\";'>Failed to add athlete to club</b><span style='display: none'><br>").append(e).append("<br></span>\n");
            }
        }
        return toReturn.toString();
    }


    /**
     * Returns the exerciseID for an input-key
     * @param key input-key from form
     * @return exerciseID from database
     */
    private int getExerciseIdFromKey(String key){
        String name;
        String unit;

        switch (key) {
            case "5000Watt":
                name = "5000";
                unit = "WATT";
                break;
            case "5000Tid":
                name = "5000";
                unit = "TIME";
                break;
            case "3000Watt":
                name = "3000";
                unit = "WATT";
                break;
            case "3000Total":
            case "3000Tid":
            case "3000m":
                name = "3000";
                unit = "TIME";
                break;
            case "2000Watt":
                name = "2000";
                unit = "WATT";
                break;
            case "2000Tid":
                name = "2000";
                unit = "TIME";
                break;
            case "60roergo":
            case "60Watt":
                name = "60";
                unit = "WATT";
                break;
            case "liggroProsent":
                name = "ligg.ro";
                unit = "PERCENT";
                break;
            case "liggroKg":
                name = "ligg.ro";
                unit = "KG";
                break;
            case "kroppshev":
                name = "kroppshevning";
                unit = "REPS";
                break;
            case "knebProsent":
                name = "knebøy";
                unit = "PERCENT";
                break;
            case "knebKg":
                name = "knebøy";
                unit = "KG";
                break;
            case "cmSargeant":
                name = "sargeant";
                unit = "CM";
                break;
            case "antBev":
            case "Beveg":
                name = "bevegelse";
                unit = "REPS";
                break;
            default:
                return 0;
        }

        return Exercises.getExerciseId(name, unit);
    }


    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Handler for mass-insert form";
    }
}

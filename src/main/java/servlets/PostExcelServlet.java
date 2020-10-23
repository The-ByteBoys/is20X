package servlets;

import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM;
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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
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

        Map<String, String[]> parameters = req.getParameterMap();
        String[] extraParameters = parameters.keySet().toArray(new String[0]);

//        out.print(extraParameters.toString());
//        return;
//    }
//    public void never(HttpServletRequest req, PrintWriter out, String[] lastNames, String[] firstNames, String[] births, String[] clubs, String[] extraParameters){

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
            out.print( addToClubs(newAthleteId, newClubs));


            // CLASS
            // TODO: insert athlete class?
            // start = birth[i]
            // insert into class_period (start, athlete, class) VALUES(


            // Results?
//            parameters.forEach((key, value) -> {

            for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
                String key = entry.getKey();

                if (!key.equals("fname") && !key.equals("lname") && !key.equals("birth") && !key.equals("clubs") && !key.equals("sex") && !key.equals("year") && !key.equals("week")) {
                    String[] values = entry.getValue();
                    if(values[i].equals("")){
                        continue;
                    }
                    // param = 5000Watt, 5000Tid, 2000Watt, 2000Tid, 60Watt, liggeroProsent, liggeroKg, osv..

                    int exerciseId = getExerciseIdFromKey(key);
                    if(exerciseId <= 0){
                        out.print(" - Didn't find exercise for key: "+key+"<br>\n");
                        continue;
                    }


                    // Format testTime
                    Calendar cal = Calendar.getInstance();
                    cal.setWeekDate(Integer.parseInt(year), Integer.parseInt(week), Calendar.MONDAY);

                    Timestamp newTime = new Timestamp(cal.getTimeInMillis());

                    // Fix time-fields
                    double newValue = 0.00;

                    if (key.matches("[0-9]+Tid")){
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
                            // e.printStackTrace();
                            out.print(" - <b onclick='this.nextElementSibling.style.display = \"initial\";'>Didn't understand input field (key: "+key+"): "+values[i]+"</b><span style='display: none'><br>"+e+"<br></span>\n");
                        }
                    }


                    try {
                        ResultModel newResult = new ResultModel(newAthleteId, exerciseId, newValue, newTime, "IP");

                        Results.addResult(newResult);
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.print(" - <b onclick='this.nextElementSibling.style.display = \"initial\";'>Failed to add result for exercise: "+key+"</b><span style='display: none'><br>"+e+"<br></span>\n");
                    }
                }
            }

            out.print("<br>\n");
        }

    }

    private int addAthlete(String fName, String lName, int birth, String sex) throws SQLException, NamingException {
        AthleteModel newAthlete = new AthleteModel(null, fName, lName, birth, sex);
        return Athletes.addAthlete(newAthlete);
    }

    private String addToClubs(int AthleteId, String[] newClubs){
        StringBuilder toReturn = new StringBuilder();
        for (String c : newClubs){
            c = c.trim();
            try {
                int newClubId = Clubs.addClub(c);
                Athletes.addAthleteToClub(AthleteId, newClubId);
                toReturn.append(" - added to club \"").append(c).append("\"");
            }
            catch(Exception e){
                e.printStackTrace();
                toReturn.append(" - <b onclick='this.nextElementSibling.style.display = \"initial\";'>Failed to add athlete to club</b><span style='display: none'><br>").append(e).append("<br></span>\n");
            }
        }
        return toReturn.toString();
    }

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
                unit = "Watt";
                break;
            case "3000Tid":
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
            case "60Watt":
                name = "60\"";
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
     *
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

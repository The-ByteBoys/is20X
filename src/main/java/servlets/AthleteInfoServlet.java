package servlets;

import enums.Athlete;
import enums.Exercise;
import enums.Result;
import models.AthleteModel;
import models.ExerciseModel;
import models.ResultModel;
import models.UserModel;
import tools.UserAuth;
import tools.htmltools.HtmlConstants;
import tools.htmltools.HtmlTableUtil;
import tools.repository.Athletes;
import tools.repository.Exercises;
import tools.repository.Results;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

// import models.AthleteModel;
// import tools.repository.Athletes;

@WebServlet(name= "AthleteInfo", urlPatterns = {"/athlete/*"})
public class AthleteInfoServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel currentUser = UserAuth.verifyLogin(request);

        writeResponseHeadless(request, response, currentUser);
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {
        out.print(HtmlConstants.getHtmlHead("Utøver - Roing Webapp", currentUser));

        String baseURI = "/roingwebapp/athlete";
        String requestArg = req.getRequestURI().substring(baseURI.length());
        try {
            requestArg = URLDecoder.decode(requestArg, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HashMap<Integer, ExerciseModel> exercises = new HashMap<>();

        if( requestArg.length() > 1 ){
            String searchName = requestArg.substring(1);
            out.print("Search for name: "+ searchName+"<br>");
            try {
                AthleteModel foundAthlete = Athletes.getAthlete(searchName);

                if(foundAthlete == null){
                    out.print("Did not find athlete "+searchName);
                    return;
                }
                int athleteId = Integer.parseInt(foundAthlete.get(Athlete.ID).toString());
                out.print("Athlete found.<br>");
                out.print("<div class='container'>");
                out.print("<h1>"+foundAthlete.get(Athlete.FNAME)+" "+foundAthlete.get(Athlete.LNAME)+"</h1>");

                // RECENT RESULTS
                out.print("Latest results: <br>");
                String lastDate = "";
                List<ResultModel> results = Results.getResultsForAthlete(athleteId);
                HtmlTableUtil resultTable = new HtmlTableUtil("");

                for(ResultModel r : results){
                    String newDate = r.get(Result.DATETIME).toString().substring(0,10);
                    if(!lastDate.equals(newDate)){
                        resultTable.newRow();
                        resultTable.addCell("<h4>"+newDate+"</h4>");
                        lastDate = newDate;
                    }

                    int exId = Integer.parseInt(r.get(Result.EXERCISEID).toString());
                    ExerciseModel ex;
                    if(exercises.containsKey(exId)){
                        ex = exercises.get(exId);
                    }
                    else {
                        ex = Exercises.getExerciseFromId(exId);
                        exercises.put(exId, ex);
                    }
                    resultTable.addHeader( ex.get(Exercise.NAME)+" "+ex.get(Exercise.UNIT));
                    resultTable.addCell(r.get(Result.RESULT).toString());
//                    resultTable.addRow(ex.get(Exercise.NAME)+": "+r.get(Result.RESULT)+" "+ex.get(Exercise.UNIT));

                }

                out.print( resultTable.toString() );
                out.print("</div>");

            }
            catch (SQLException e){
                e.printStackTrace();
                out.print("Something went wrong.");
//                out.print("Did not find athlete "+searchName);
            }

        }
        else {
            out.print("Please enter an athletes name. "+requestArg);
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

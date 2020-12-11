package servlets;

import enums.Result;
import enums.UserLevel;
import models.ResultModel;
import models.UserModel;
import tools.UserAuth;
import tools.htmltools.HtmlConstants;
import tools.repository.Results;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * PublishResultServlet publishes the best results from the recent test-period.
 * @author Johannes Birkeland
 */
@WebServlet(name = "PublishResult", urlPatterns = {"/publishResults"})
public class PublishResultServlet extends AbstractAppServlet{


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
        writeResponseHeadless(request, response, currentUser);
    }


    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {
        out.print(HtmlConstants.getHtmlHead("Test registrert - Roing Webapp", currentUser));

        String publish = req.getParameter("publish");
        String[] bestResults = req.getParameterValues("bestResults");

        out.println("<form action='publishResults' method='post'>");

        try {
            List<ResultModel> results = Results.getBestResultsInTestBattery();
            for (ResultModel rm : results) {
                int athleteId = (int) rm.get(Result.ATHLETEID);
                int exerciseId = (int) rm.get(Result.EXERCISEID);
                double result = (double) rm.get(Result.RESULT);
                String testDate = rm.get(Result.DATETIME).toString().substring(0, 19);

                out.println("<label>" +
                            "<input type='checkbox' name='bestResults' value='" + athleteId + "  " + exerciseId + "  " + testDate +"' checked> " +
                            athleteId + " " + exerciseId + " " + result + " " + testDate +
                            "</label>" +
                            "<br>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("<input name='publish' type='submit'>");
        out.println("</form>");

        if (publish != null) {
            /**
             *
            try {
                for (String bestResult : bestResults) {
                    String[] attributes = bestResult.split("/\\s\\s/");
                    Results.publishResult(Integer.parseInt(attributes[0]), Integer.parseInt(attributes[1]), attributes[2]);
                    out.println(bestResult);
                }
                out.println("Resultatene er publisert!");
                out.println(Arrays.toString(bestResults));
            } catch (Exception e) {
                out.println("Noe gikk galt!");
            }
             **/




        }
    }


}

package servlets;

import enums.UserLevel;
import models.UserModel;
import tools.DbTool;
import tools.UserAuth;
import tools.htmltools.HtmlConstants;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

@WebServlet(name= "SubmitTestServlet", urlPatterns = {"/submit-tests"})
public class SubmitTestServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
        writeResponseHeadless(request, response, currentUser);
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {
        out.print(HtmlConstants.getHtmlHead("Test registrert - Roing Webapp", currentUser));
        out.print("<div class=\"container-fluid\" style=\"text-align: left; overflow-x: auto; padding-bottom: 20px;\" id='tableHolder'>");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("CET"));
        String DATE = formatter.format(date);

        String[] ids = req.getParameterValues("ids");
        ArrayList<String> idsList = new ArrayList<>(Arrays.asList(ids));


        //TODO: GJØR SOM EIRIK SIER - koble til database via models og repository.
        try {
            Connection db = DbTool.getINSTANCE().dbLoggIn();
            String query = "INSERT INTO result (athlete, exercise, result, date_time, result_Type)\n" +
                    "VALUES (?, ?, ?, ?, 'NP')";
            PreparedStatement pstm = db.prepareStatement(query);


        for (String id : idsList) {
            String[] idArray = id.split("-");
            int ex_id = Integer.parseInt(idArray[0]);
            int at_id = Integer.parseInt(idArray[1]);
            String ex_unit = idArray[2];
            double result;

            if (ex_unit.contains("TIME")) {
                double minutes = Double.parseDouble(req.getParameter(at_id + "resultMin")) * 60;
                double seconds = Double.parseDouble(req.getParameter(at_id + "resultSec"));
                result = minutes + seconds;

            } else {
                result = Double.parseDouble(req.getParameter(at_id + "result"));
            }

            pstm.setInt(1, at_id);
            pstm.setInt(2, ex_id);
            pstm.setDouble(3, result);
            pstm.setString(4, DATE);
            pstm.executeUpdate();
        }
        db.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        out.println("<p>Resultater registrert!</p>");


    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }
}

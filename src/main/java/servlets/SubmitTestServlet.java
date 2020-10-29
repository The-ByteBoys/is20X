package servlets;

import tools.DbTool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name= "SubmitTestServlet", urlPatterns = {"/submit-tests"})
public class SubmitTestServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponse(request, response, "Submit tests");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("CET"));
        String DATE = formatter.format(date);

        String[] ids = req.getParameterValues("ids");
        ArrayList<String> idsList = new ArrayList<>(Arrays.asList(ids));

        try {
            Connection db = DbTool.getINSTANCE().dbLoggIn();
            String query = "INSERT INTO result (athlete, exercise, result, date_time, result_Type)\n" +
                    "VALUES (?, ?, ?, ?, 'NP')";
            PreparedStatement pstm = db.prepareStatement(query);


        for (String id : idsList) {
            String[] idArray = id.split("-");
            int ex_id = Integer.parseInt(idArray[0]);
            int at_id = Integer.parseInt(idArray[1]);
            int result = Integer.parseInt(req.getParameter(at_id + "result"));

            pstm.setInt(1, at_id);
            pstm.setInt(2, ex_id);
            pstm.setInt(3, result);
            pstm.setString(4, DATE);
            /*
            String queryPart = "(" + at_id + ", " + ex_id + ", " + result + ", " + "'"+DATE+"'" + ", 'NP')";

            if (id.equals(idsList.get(idsList.size() - 1))) {
                queryPart += ";\n";
            } else {
                queryPart += ",\n";
            }

             */
            pstm.executeUpdate();
        }
        db.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}

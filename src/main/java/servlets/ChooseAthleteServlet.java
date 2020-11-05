package servlets;

import enums.UserLevel;
import models.UserModel;
import tools.UserAuth;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name= "ChooseAthleteServlet", urlPatterns = {"/choose-athlete"})
public class ChooseAthleteServlet extends AbstractAppServlet{
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);
        writeResponse(request, response, "Choose Athletes");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;

        try {
            String[] athletes = request.getParameterValues("athletes");
            List<String> list = Arrays.asList(athletes);
            request.setAttribute("athletes", list);
            rd = request.getRequestDispatcher("submitTest.jsp");
            rd.forward(request, response);


        } catch (Exception e) {
            rd = request.getRequestDispatcher("/chooseAthlete.jsp");
            rd.forward(request, response);

            //TODO: give feedback as in the mypage.jsp (userAuth) when something is wrong.
        }
    }
}

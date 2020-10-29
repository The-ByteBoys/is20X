package servlets;

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
            String seniorTest = request.getParameter("SENIOR + \"-\" + name+unit");
            String[] athletes = request.getParameterValues("athletes");
            List<String> list = Arrays.asList(athletes);
            request.setAttribute("athletes", list);
            rd = request.getRequestDispatcher("submitTest_testRegister.jsp");
            rd.forward(request, response);


        } catch (NullPointerException e) {
            request.setAttribute("errorMessage", "Velg minst en utøver");
            rd = request.getRequestDispatcher("/chooseAthlete.jsp");
            rd.forward(request, response);
            //TODO: get this exception handler to work
        }
    }
}

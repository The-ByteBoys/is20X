package servlets;

import enums.Club;
import models.ClubModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterClub", urlPatterns = {"/clubregistration"})
public class RegisterClub extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponse(request, response, "Register Club");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {

        out.print("<h1>Registering club...</h1>");

        String name = req.getParameter("clubName");

        ClubModel newClub = new ClubModel();
        newClub.set(Club.NAME, name);

        out.print("Club registered!");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

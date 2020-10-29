package servlets;

import enums.Club;
import models.ClubModel;

import tools.html.htmlConstants;
import tools.repository.Clubs;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

@WebServlet(name = "RegisterClub", urlPatterns = {"/clubregistration"})
public class RegisterClub extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        writeResponseHeadless(request, response);
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {

        out.print(htmlConstants.getHtmlHead("Club registration"));
        out.print("<div class=\"container-fluid\" style=\"text-align: left; overflow-x: auto; padding-bottom: 20px;\" id='tableHolder'>");

        String name = req.getParameter("clubname");

        try {
            Clubs.addClub(name);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        out.print("Club with name ´" + name + "´ registered!");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

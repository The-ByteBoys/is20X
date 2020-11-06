package servlets;

import enums.Club;
import enums.UserLevel;
import models.UserModel;
import tools.UserAuth;
import tools.htmltools.HtmlConstants;
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
        UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.ADMIN);

        if( currentUser != null ){
            writeResponseHeadless(request, response, currentUser);
        }
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {

        out.print(HtmlConstants.getHtmlHead("Club registration"));
        out.print("<div class=\"container-fluid\" style=\"text-align: left; overflow-x: auto; padding-bottom: 20px;\" id='tableHolder'>");

        String name = req.getParameter("clubname");

        int newClubId = 0;
        try {
            newClubId = Clubs.addClub(name);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        out.print("Club ("+newClubId+") with name ´" + name + "´ registered!");
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

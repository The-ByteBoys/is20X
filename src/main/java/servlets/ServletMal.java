package servlets;

import enums.UserLevel;
import models.UserModel;
import tools.UserAuth;
import tools.htmltools.HtmlConstants;

import java.io.IOException;
import java.io.PrintWriter;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import models.AthleteModel;
// import tools.repository.Athletes;

@WebServlet(name= "Servlet", urlPatterns = {"/mal"})
public class ServletMal extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);

        writeResponseHeadless(request, response, currentUser);
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {
        out.print(HtmlConstants.getHtmlHead("Template - Roing Webapp", currentUser));

        // CODE HERE
        
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

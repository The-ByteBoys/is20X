package servlets;

import enums.Athlete;
import enums.Club;
import enums.UserLevel;
import models.AthleteModel;
import models.ClubModel;
import models.UserModel;
import tools.UserAuth;
import tools.htmltools.HtmlConstants;
import tools.repository.Athletes;
import tools.repository.Clubs;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// import models.AthleteModel;
// import tools.repository.Athletes;

@WebServlet(name= "Search", urlPatterns = {"/search"})
public class SearchServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel currentUser = UserAuth.verifyLogin(request);

        writeResponseHeadless(request, response, currentUser);
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {
        out.print(HtmlConstants.getHtmlHead("Search - Roing Webapp", currentUser));

        String search = req.getParameter("search");

        out.print("<div class='container'>");

        out.print("<h1>Search</h1>");
        out.print("<form action='search' method='post' class='input-group' style='margin: auto; width: 70vw; max-width: 500px;'>" +
                "<input type='text' name='search' class='form-control'"+(search != null?" value='"+search+"'":"")+" style='text-align: center;'> " +
                "<div class='input-group-append'><input type='submit' class='btn btn-outline-secondary' value='Search'></div>" +
                "</form>");
        if(search != null && search.length() > 0){
            out.print("<hr><h4>Searchresults for \""+search+"\":</h4><br>");

            out.print("<div class=\"list-group\" style='width: 80vw; max-width: 600px;margin: auto;'>");

            // SEARCH ATHLETES
            try {
                List<AthleteModel> foundAthletes = Athletes.findAthletes(search);
                for(AthleteModel a : foundAthletes){
                    String fullName = a.get(Athlete.FNAME)+" "+a.get(Athlete.LNAME);
                    String birth = a.get(Athlete.BIRTH).toString().substring(0,4);
                    out.print("<a href='./athlete/"+fullName+"' class='list-group-item list-group-item-action justify-content-between align-items-center d-flex'>" +
                            "<span>"+fullName+
                                (!birth.equals("0001")?" <span style='font-size: 12px; padding-left: 10px;'>("+birth+")</span>":"")+
                            "</span>" +
                            "<span class='badge badge-primary badge-pill'>Utøver</span>" +
                            "</a>");
                }

                List<ClubModel> foundClubs = Clubs.findClubs(search);
                for(ClubModel c : foundClubs){
                    String clubName = c.get(Club.NAME).toString();
                    out.print("<a href='./club/"+clubName+"' class='list-group-item list-group-item-action justify-content-between align-items-center d-flex'>"+clubName+"<span class='badge badge-warning badge-pill'>Klubb</span></a>");
                }

            }
            catch (SQLException e){
                out.print("No athletes found.");
            }
            out.print("</div>");
        }

        out.print("");
        out.print("</div>");
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

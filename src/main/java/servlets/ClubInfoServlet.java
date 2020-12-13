package servlets;

import enums.Athlete;
import enums.Club;
import models.*;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name= "ClubInfo", urlPatterns = {"/club/*"})
public class ClubInfoServlet extends AbstractAppServlet {


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserModel currentUser = UserAuth.verifyLogin(request);
        writeResponseHeadless(request, response, currentUser);
    }


    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser) {
        out.print(HtmlConstants.getHtmlHead("Klubb - Roing Webapp", currentUser));

        String baseURI = "/roingwebapp/club";
        String requestArg = req.getRequestURI().substring(baseURI.length());
        try {
            requestArg = URLDecoder.decode(requestArg, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if( requestArg.length() > 1 ){
            String searchName = requestArg.substring(1);
            try {
                ClubModel foundClub = Clubs.getClub(searchName);

                if(foundClub == null){
                    out.print("Club not found!");
                    return;
                }

                int clubID = (int) foundClub.get(Club.ID);

                out.print("<div class='container'>");
                out.print("<h5>Klubb</h5>");
                out.print("<h1>"+foundClub.get(Club.NAME)+"</h1>");

                List<AthleteModel> clubAthletes = Athletes.getAthletes(clubID);

                out.print("<br>");
                out.print("<h4>Utøvere:</h4>");
                for(AthleteModel athlete : clubAthletes){
                    String athleteName = athlete.get(Athlete.FNAME).toString()+" "+athlete.get(Athlete.LNAME).toString();
                    out.print("<a href='../athlete/"+athleteName+"'>"+athleteName+"</a><br>");
                }

                out.print("</div>");
            }
            catch (SQLException e){
                e.printStackTrace();
                out.print("Something went wrong.");
            }

        }
        else {
            out.print("Please enter a club name.");
        }
    }


    /**
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Club-info page";
    }
}

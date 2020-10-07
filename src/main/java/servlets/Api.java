package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.*;
import tools.repository.Athletes;
import tools.repository.Clubs;
// import tools.CustomException;
// import tools.repository.UserRepository;

@WebServlet(name= "Api", urlPatterns = {"/api/*"})
public class Api extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        writeResponseJson(request, response, "Roing Webapp API");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        
        String baseURI = "/roingwebapp/api";

        // out.print(req.getRequestURI().substring(baseURI.length()));
        
        if( req.getRequestURI().substring(baseURI.length()).matches("/klubb($|/)(.*)") ){

            String[] uriparts = req.getRequestURI().split("/");
            String lastPart = uriparts[ uriparts.length-1 ];

            lastPart = lastPart.replace("%20", " ");

            try{
                ClubModel club = Clubs.getClub( lastPart );

                if(club != null){
                    out.print("{ \"data\": [");
                    out.print( club.toString() );
                    out.print("]}");
                }
                else {
                    out.print( "{ \"status\": { \"error\": \"Failed\", \"errorMsg\": \"Club not found!\" } }" );
                }
            }
            catch( SQLException e ){
                out.print( "{ \"status\": { \"error\": \"Failed\", \"errorMsg\": \"Exception: "+e.toString().replace("\"", "\\\"")+"\" } }" );
            }
            
        } else if( req.getRequestURI().substring(baseURI.length()).matches("/klubber($|/)") ){

            try {
                List<ClubModel> clubs = Clubs.getClubs();
                ArrayList<String> output = new ArrayList<>();
                
                for( ClubModel c : clubs ){
                    output.add( "\n"+c.toString() );
                }

                out.print("{ \"data\": [" + String.join(",", output) + "]}");
            }
            catch( SQLException e ){
                e.printStackTrace();
                out.print( "{ \"status\": { \"error\": \"Failed\", \"errorMsg\": \"SQL Exception: "+e.toString().replace("\"", "\\\"")+"\" } }" );
            }
            
        } else if( req.getRequestURI().substring(baseURI.length()).matches("/ut(.*)ver($|/)(.*)") ){ // True for "api/utover/" and "api/ut%C3%B8ver/" but not "api/utovere/" etc.
            /** %C3%B8 = ø */

            String[] uriparts = req.getRequestURI().split("/");
            String lastPart = uriparts[ uriparts.length-1 ];

            lastPart = lastPart.replace("%20", " ");

            try{
                AthleteModel athlete = Athletes.getAthlete( lastPart );

                if(athlete != null){
                    out.print("{ \"data\": [");
                    out.print( athlete.toString() );
                    out.print("]}");
                }
                else {
                    out.print( "{ \"status\": { \"error\": \"Failed\", \"errorMsg\": \"Athlete not found!\" } }" );
                }
            }
            catch( SQLException e ){
                out.print( "{ \"status\": { \"error\": \"Failed\", \"errorMsg\": \"Exception: "+e.toString().replace("\"", "\\\"")+"\" } }" );
            }
            
        } else if( req.getRequestURI().substring( baseURI.length() ).matches("/ut(?:%C3%B8|o)vere($|/)") ){

            try {
                List<AthleteModel> atheles = Athletes.getAthletes();
                ArrayList<String> output = new ArrayList<>();
                
                for( AthleteModel a : atheles ){
                    output.add( "\n"+a.toString() );
                }

                out.print("{ \"data\": [" + String.join(",", output) + "]}");
            }
            catch( SQLException e ){
                out.print( "{ \"status\": { \"error\": \"Failed\", \"errorMsg\": \"SQL Exception: "+e.toString().replace("\"", "\\\"")+"\" } }" );
            }

        } else if( req.getRequestURI().matches(baseURI+"($|/)") ){
            out.print( "{ \"data\": { \"links\": [{ \"rel\": \"list utøvere\", \"uri\": \""+req.getRequestURL()+"/ut%C3%B8vere/\" }, { \"rel\": \"list klubber\", \"uri\": \""+req.getRequestURL()+"/klubber/\" }] } }" );
        } else {
            out.print( "{ \"status\": { \"error\": \"Failed\" } }" );
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
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

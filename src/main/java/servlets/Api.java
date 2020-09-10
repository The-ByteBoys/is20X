package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.AthleteModel;
import models.UserModel;
import tools.repository.Athletes;
import tools.repository.UserRepository;

@WebServlet(name= "Api", urlPatterns = {"/api/*"})
public class Api extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        writeResponseJson(request, response, "Roing Webapp API");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        // out.print(  );
        
        Boolean oneIsPrinted = false;

        /** %C3%B8 = Ø */
        if( req.getRequestURI().substring( ("/roingwebapp/api").length() ).equals("/ut%C3%B8vere") ){
            out.print("{ \"data\": [");
            List<AthleteModel> atheles = Athletes.getAthletes(out);
            for( AthleteModel a : atheles ){
            // while(atheles.hasNext()){
                if(oneIsPrinted){
                    out.print(",");
                }
                out.print( a.toString() );

                oneIsPrinted = true;
                // out.print( "}" );
            }
            out.print("]}");
        } else {
            out.print( "{ \"data\": { \"links\": [{ \"rel\": \"list\", \"uri\": \"api/utøvere/\" }] } }" );
        }

        // String username = req.getParameter("uname");
        // String username = "admin@roro";
        // String nameFromDb = UserRepository.getUserName(username,out);

        // UserModel user = UserRepository.getUser(username, out);

        // out.print( user.toString() );
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
    //  */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

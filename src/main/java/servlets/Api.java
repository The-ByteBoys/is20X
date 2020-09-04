package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.UserModel;
import tools.repository.UserRepository;

@WebServlet(name= "Api", urlPatterns = {"/api"})
public class Api extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        writeResponseJson(request, response, "Roing Webapp API");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        // String username = req.getParameter("uname");
        String username = "admin@roro";
        // String nameFromDb = UserRepository.getUserName(username,out);

        UserModel user = UserRepository.getUser(username, out);

        out.print( user.toString() );
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

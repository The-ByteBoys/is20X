package servlets;


import models.UserModel;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author evenal
 */
public abstract class AbstractAppServlet extends HttpServlet {

    public static final String HTML_PAGE_START
        = "<!DOCTYPE html>\n<html lang=\"no\">\n<head>\n  "
        + "<meta charset=\"UTF-8\">\n  "
        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n  "
        + "<title>%s</title>\n</head>\n<body>\n";
    public static final String HTML_PAGE_END
        = "</body>\n</html>\n";

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    protected void writeResponse(HttpServletRequest request, HttpServletResponse response, String title) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.format(HTML_PAGE_START, title);
            writeBody(request, out);
            out.format(HTML_PAGE_END);
        }
    }
    protected PrintWriter getWriteResponse(HttpServletResponse response, String title) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.format(HTML_PAGE_START, title);
            return out;
        }
    }

    protected void writeResponseJson(HttpServletRequest request, HttpServletResponse response, String title) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            writeBody(request, out);
        }
    }

    protected void writeResponseHeadless(HttpServletRequest request, HttpServletResponse response, UserModel currentUser) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if(currentUser != null){
                writeBody(request, out, currentUser);
            }
            else {
                writeBody(request, out);
            }
        }
    }


    protected void writeBody(HttpServletRequest req, PrintWriter out){
        writeBody(req, out, null);
    }
    protected void writeBody(HttpServletRequest req, PrintWriter out, UserModel currentUser){
        writeBody(req, out);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

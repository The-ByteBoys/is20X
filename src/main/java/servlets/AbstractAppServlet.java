package servlets;


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
        = "<html>\n<head>\n"
        + "<title>%s</title>\n</head>\n<body>\n";
    public static final String HTML_PAGE_END
        = "</body>\n</html>";

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    protected void writeResponse(HttpServletRequest request,
        HttpServletResponse response,
        String title)
        throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.format(HTML_PAGE_START, title);
            writeBody(request, out);
            out.format(HTML_PAGE_END);
        }
    }

    protected abstract void writeBody(HttpServletRequest req,
        PrintWriter out);

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

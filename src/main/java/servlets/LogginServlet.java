package servlets;

import models.UserModel;
import tools.UserAuth;
import tools.repository.UserRepository;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.xml.registry.infomodel.User;

@WebServlet(name= "LogginServlet", urlPatterns = {"/login"})
public class LogginServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        writeResponse(request, response, "Hello!");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        //String username = req.getParameter("uname");
        //String nameFromDb = UserRepository.getUserName(username, out);
        //out.format("<h1> Here is your request: %s</h1", nameFromDb);

    }

    //private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("email");
        String password = request.getParameter("password");
        response.setContentType("text/html;charset=UTF-8");
        UserModel user = null;

        try (PrintWriter out = response.getWriter()) {
            try {
                user = UserAuth.checkLogin(username, password, response);
                if (user == null) {
                    out.print("Login failed");
                } else {
                    out.print("Login successfull");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.print("Exception: " + e);
            }
        }
    }
}
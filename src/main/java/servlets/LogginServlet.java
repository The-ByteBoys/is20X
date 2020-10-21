package servlets;

import tools.PasswordEncrypt;
import tools.UserAuth;
import java.io.*;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name= "LogginServlet", urlPatterns = {"/login"})
public class LogginServlet extends AbstractAppServlet {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        // Never used
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            PrintWriter out = response.getWriter();
            if(UserAuth.checkLogin(username, password)){
                /* SUCCESSFUL LOGIN */
                Cookie ck = new Cookie("auth", PasswordEncrypt.lagToken());
                ck.setMaxAge(600);

                response.addCookie(ck);
                response.sendRedirect("home.jsp");
            }
            else {
                out.print("Login failed! <a href='login.jsp'>Login</a>.");
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
package servlets;

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
            String token = UserAuth.checkLogin(username, password);

            if(token != null){
                /* SUCCESSFUL LOGIN */
                Cookie ck = new Cookie("auth", token);
                ck.setMaxAge(1800);

                response.addCookie(ck);
                response.sendRedirect("mypage.jsp");
            }
            else {
                HttpSession session = request.getSession();
                session.setAttribute("error", "Login failed. Please check your username and password and try again!");
                response.sendRedirect("login.jsp");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
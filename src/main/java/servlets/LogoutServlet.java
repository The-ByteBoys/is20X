package servlets;

import tools.UserAuth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name= "Logout Servlet", urlPatterns = {"/logout"})
public class LogoutServlet extends AbstractAppServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie ck=new Cookie("auth","un");
        ck.setMaxAge(0);
        response.addCookie(ck);

        HttpSession session = request.getSession();
        session.setAttribute("msg", "Logout successfull!");
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        // Never used
    }

}
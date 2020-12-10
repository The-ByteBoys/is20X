package servlets;

import tools.UserAuth;
import tools.repository.UserRepository;

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
        // Delete token
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                String name = ck.getName();
                String value = ck.getValue();
                if (name.equals("auth")) {
                    UserRepository.deleteToken(value);
                }
            }
        }

        // Expire cookie
        Cookie ck=new Cookie("auth","un");
        ck.setMaxAge(0);
        response.addCookie(ck);

        // Notify that logout was successful
        HttpSession session = request.getSession();
        session.setAttribute("msg", "Logout successfull!");
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void writeBody(HttpServletRequest req, PrintWriter out) {
        // Never used
    }

}
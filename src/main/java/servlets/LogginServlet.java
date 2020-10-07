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

        String username = req.getParameter("email");
        String password = req.getParameter("password");

        UserModel user = null;

        out.print("<h1>Login!</h1>");

        try {
            user = UserAuth.checkLogin(username, password);
            out.print(user.toString());
            out.print("<br>Login complete!");
        }
        catch( SQLException e){
            e.printStackTrace();
            out.print("Exception: "+e);
        }

        if(user == null){
            out.print("Login failed");
        }

    }

    private static final long serialVersionUID = 1L;

    public LogginServlet() {
        super();
    }

    /*
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        /* userDAO userDao = new userDAO();

        try {
            User user = userDao.checkLogin(username, password);
            String destPage = "login.jsp";

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                destPage = "home.jsp";
            } else {
                String message = "Invalid username/password";
                request.setAttribute("message", message);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
            dispatcher.forward(request, response);

        } catch (SQLException | ClassNotFoundException ex) {
            throw new ServletException(ex);
        }
    }*/
}
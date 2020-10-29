package servlets;

import enums.User;
import enums.UserLevel;
import models.UserModel;
import tools.UserAuth;
import tools.htmltools.HtmlConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterUser", urlPatterns = {"/userregistration"})
public class RegisterUser extends AbstractAppServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserModel currentUser = UserAuth.requireLogin(request, response, UserLevel.COACH);

        HttpSession session = request.getSession();

        if(currentUser == null){
            return;
        }

        String email=request.getParameter("userEmail");
        String pass=request.getParameter("userPass");
        String type=request.getParameter("userType");

        UserModel newUser = new UserModel();
        newUser.set(User.EMAIL, email);
        newUser.set(User.PASSWORD, pass);
        newUser.set(User.TYPE, type);

        try {
            int newID = UserAuth.opprettBruker(newUser);
            session.setAttribute("msg", "Succesfully added user with id: "+newID);
        }
        catch(Exception e){
            e.printStackTrace();
            session.setAttribute("error", "Failed to add user");
        }

        response.sendRedirect("register.jsp");
    }

    /**
     * Returns a short description of the servlet.
     *
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}


package servlets;

import enums.User;
import enums.UserLevel;
import models.AthleteModel;
import models.UserModel;
import tools.UserAuth;
import tools.repository.Athletes;
import tools.repository.Clubs;
import tools.repository.UserRepository;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

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

        String fName = request.getParameter("userFname");
        String lName = request.getParameter("userLname");
        String birthStr = request.getParameter("userBirth");
        String sex = request.getParameter("userSex");
        String club = request.getParameter("userClub");

        if(
                email == null || pass == null || type == null ||
                (!type.equals("ADMIN") && (fName == null || lName == null || birthStr == null || sex == null || club == null))
        ){
            session.setAttribute("msg", "Empty fields. Not continuing!");
            response.sendRedirect("register.jsp");
            return;
        }

        Integer birth = Integer.parseInt(birthStr);

        UserModel newUser = new UserModel();
        newUser.set(User.EMAIL, email);
        newUser.set(User.PASSWORD, pass);
        newUser.set(User.TYPE, type);

        int newUserID;
        try {
            newUserID = UserAuth.createUser(newUser);

            if(!type.equals("ADMIN")){

                // ADD ATHLETE IF NOT EXIST
                AthleteModel newAthelte = new AthleteModel(null, fName, lName, birth, sex);
                int athleteId = Athletes.addAthlete(newAthelte);

                // ADD CLUB IF NOT EXIST
                int clubId = Clubs.addClub(club);

                // ADD ATHLETE TO CLUB
                Athletes.addAthleteToClub(athleteId, clubId);

                // ADD USER TO CLUB_USERS
                UserRepository.connectUserAndAthlete(newUserID, athleteId);
            }


            session.setAttribute("msg", "Succesfully added user with id: "+newUserID);
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


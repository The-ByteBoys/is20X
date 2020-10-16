package tools;

import enums.*;
import models.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuth {
    public static UserModel checkLogin(String username, String password, HttpServletResponse response) throws SQLException, IOException {

        UserModel userModel = null;
        try {
            Connection db = null;
            PreparedStatement statement = null;

            db = DbTool.getINSTANCE().dbLoggIn();

            ResultSet rs = null;

            statement = db.prepareStatement("SELECT * FROM user WHERE email = ? AND pass = ?");
            statement.setString(1, username);
            statement.setString(2, PasswordEncrypt.getKrypterPassord(password));
            rs = statement.executeQuery();

            if (rs.next()) {
                userModel = new UserModel();
                userModel.set(User.FNAME, rs.getString("fname"));
                userModel.set(User.LNAME, rs.getString("lname"));
            }

            rs.close();
            db.close();

            if(userModel != null){
                // successfull login
                PrintWriter out = response.getWriter();
                Cookie ck = new Cookie("auth", username);
                ck.setMaxAge(600);

                response.addCookie(ck);
                response.sendRedirect("home.jsp");
            }
        }
        catch(SQLException | IOException e){
            throw e;
        }
        return userModel;
    }

    public static boolean verifyLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (int i = 0; i < cks.length; i++) {
                String name = cks[i].getName();
                String value = cks[i].getValue();
                if (name.equals("auth")) {
                    return true; // exit the loop and continue the page
                }
                if (i == (cks.length - 1)) // if all cookie are not valid redirect to error page
                {
                    response.sendRedirect("login.jsp");
                    return false; // to stop further execution
                }
                i++;
            }
        } else {
            response.sendRedirect("login.jsp");
            return false; // to stop further execution
        }
        return false;
    }

}

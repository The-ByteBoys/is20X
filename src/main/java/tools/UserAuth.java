package tools;

import models.UserModel;
import tools.repository.UserRepository;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuth {
    public static String checkLogin(String username, String password) throws SQLException {
        Connection db = null;
        PreparedStatement statement = null;
        String toReturn = null;

        db = DbTool.getINSTANCE().dbLoggIn();

        ResultSet rs = null;

        statement = db.prepareStatement("SELECT user_id, email FROM user WHERE email = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, PasswordEncrypt.getKrypterPassord(password));
        rs = statement.executeQuery();

        while(rs.next()){
            if(rs.getString("email").equals(username)) {
                toReturn = PasswordEncrypt.lagToken();

                try {
                    UserRepository.setUserToken(rs.getInt("user_id"), toReturn);
                } catch (NamingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        rs.close();
        db.close();

        return toReturn;
    }

    public static UserModel verifyLogin(HttpServletRequest request) {
        UserModel user = null;
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                String name = ck.getName();
                String value = ck.getValue();
                if (name.equals("auth")) {
                    try {
                        user = UserRepository.getUserFromToken(value);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    return user;
                }
            }
        }

        return null;
    }

    public static UserModel requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserModel user = null;
        user = verifyLogin(request);
        if( user == null ){
            response.sendRedirect("login.jsp");
        }

        return user;
    }

}

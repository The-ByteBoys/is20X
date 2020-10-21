package tools;

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

        statement = db.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, PasswordEncrypt.getKrypterPassord(password));
        rs = statement.executeQuery();

        while(rs.next()){
            if(rs.getString("email").equals(username)) {
                toReturn = PasswordEncrypt.lagToken();

                // TODO: Legg token inn i databasen
            }
        }

        rs.close();
        db.close();

        return toReturn;
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

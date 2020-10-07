package tools;

import enums.*;
import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuth {
    public static UserModel checkLogin(String username, String password) throws SQLException {

        UserModel userModel = null;
        try {
            Connection db = null;
            PreparedStatement statement = null;

            db = DbTool.getINSTANCE().dbLoggIn();

            ResultSet rs = null;

            statement = db.prepareStatement("SELECT * FROM user WHERE email = ? AND pass = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            rs = statement.executeQuery();

            if (rs.next()) {
                userModel = new UserModel();
                userModel.set(User.FNAME, rs.getString("fname"));
                userModel.set(User.LNAME, rs.getString("lname"));
            }
            rs.close();
            db.close();
        }
        catch(SQLException e){
            throw e;
        }

        return userModel;

    }
}

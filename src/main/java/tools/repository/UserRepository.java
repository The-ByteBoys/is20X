package tools.repository;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.UserModel;
import tools.DbTool;

public class UserRepository {

public static void addUser(UserModel user, PrintWriter p) {
    Connection db = null;
    PreparedStatement insertNewUser = null;
    try {
        db = DbTool.getINSTANCE().dbLoggIn(p);
        String query = "INSERT INTO `user` (User_firstName, User_lastName,User_Email, User_password ) values (?,?,?,?)";

        insertNewUser = db.prepareStatement(query);
        insertNewUser.setString(1,user.getFirstName());
        insertNewUser.setString(2,user.getLastName());
        insertNewUser.setString(3,user.getUserName());
        insertNewUser.setString(4,user.getPassword());
        insertNewUser.execute();



    } catch (SQLException throwables) {
        throwables.printStackTrace();
    } finally {
        try {
            db.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}


public static String getUserName(String username, PrintWriter p) {
    Connection db = null;
    PreparedStatement prepareStatement = null;

    String toReturn = null;
     try {
        db = DbTool.getINSTANCE().dbLoggIn(p);
        ResultSet rs = null;
        String query = "SELECT * FROM otra.user where User_Email = ?";
        prepareStatement =  db.prepareStatement(query);
        prepareStatement.setString(1, username);
        rs = prepareStatement.executeQuery();
        while (rs.next()) {
            toReturn = rs.getString("User_Email");
        }
        rs.close();

     } catch (SQLException throwables) {
         throwables.printStackTrace();
     }
     return toReturn;
}
}
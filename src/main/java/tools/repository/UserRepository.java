package tools.repository;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.UserModel;
import tools.DbTool;
import enums.*;

public class UserRepository {

    /*public static void addUser(UserModel user, PrintWriter p) {
        Connection db = null;
        PreparedStatement insertNewUser = null;
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            String query = "INSERT INTO `user` (fName, lName, email, password) values (?,?,?,?)";

            insertNewUser = db.prepareStatement(query);
            insertNewUser.setObject(1,user.get(User.FNAME));
            insertNewUser.setObject(2,user.get(User.LNAME));
            insertNewUser.setObject(3,user.get(User.EMAIL));
            insertNewUser.setObject(4,user.get(User.PASSWORD));
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

    }*/

    public static String getUserName(String username, PrintWriter p) {
        Connection db = null;
        PreparedStatement prepareStatement = null;

        String toReturn = null;
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "SELECT * FROM roro.user where email = ?";
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

    /*public static UserModel getUser(String username){
        Connection db = null;
        PreparedStatement prepareStatement = null;

        UserModel toReturn = null;
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "SELECT email FROM roro.user WHERE email = ?";
            prepareStatement = db.prepareStatement(query);
            prepareStatement.setString(1, username);
            rs = prepareStatement.executeQuery();
            while(rs.next()){
                toReturn = new UserModel(rs.getString("fname"), rs.getString("lname"), rs.getString("email"), "secret");
            }

            db.close();
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return toReturn;
    }*/

}
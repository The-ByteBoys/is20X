package tools.repository;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.UserModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;
import enums.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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

    public static void setUserToken(int user_id, String token) throws NamingException {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", user_id);
        parameters.put("loginToken", token);

        Context ctx = new InitialContext();
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("userLogin").usingGeneratedKeyColumns("created");
        insert.execute(parameters);
    }

    public static UserModel getUserFromToken(String token) throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;

        UserModel user = null;
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "SELECT user FROM userLogin WHERE loginToken = ?";
            prepareStatement =  db.prepareStatement(query);
            prepareStatement.setString(1, token);
            rs = prepareStatement.executeQuery();
            while (rs.next()) {
                user = getUserFromId(rs.getInt("user"));
            }
            rs.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    public static UserModel getUserFromId(int userid) throws SQLException {
        UserModel user = null;

        try {
            String query = "SELECT email, password, userType FROM user u WHERE user_id = '"+userid+"'";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
                user = new UserModel(userid, rs.getString("email"), rs.getString("userType"), null);
            }

            rs.close();

        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return user;
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
package tools.repository;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.AthleteModel;
import models.UserModel;
import tools.DbTool;

public class Athletes {

    public static List<AthleteModel> getAthletes() throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;

        List<AthleteModel> toReturn = new ArrayList<>();

        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "SELECT * FROM roro.athletes";
            prepareStatement =  db.prepareStatement(query);
            rs = prepareStatement.executeQuery();

            while (rs.next()) {
                AthleteModel athlete = new AthleteModel(rs.getInt("id"), rs.getString("name"), rs.getInt("birth"), rs.getInt("club"));
                toReturn.add(athlete);
            }
            rs.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

        return toReturn;
    }

    public static UserModel getUser(String username){
        Connection db = null;
        PreparedStatement prepareStatement = null;

        UserModel toReturn = new UserModel();
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "SELECT * FROM roro.users WHERE email = ?";
            prepareStatement = db.prepareStatement(query);
            prepareStatement.setString(1, username);
            rs = prepareStatement.executeQuery();
            while(rs.next()){
            
                toReturn.setFirstName(rs.getString("fname"));
                toReturn.setLastName(rs.getString("lname"));
                toReturn.setUserName(rs.getString("email"));
                toReturn.setPassword("secret");
                // toReturn.setLastName();
            }

            db.close();
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return toReturn;
    }

}
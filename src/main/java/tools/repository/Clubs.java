package tools.repository;

// import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import enums.*;
import models.ClubModel;
// import models.UserModel;
import tools.DbTool;
// import tools.CustomException;

public class Clubs {

    public static List<ClubModel> getClubs() throws SQLException {
        List<ClubModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT club_id, name FROM club;";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while (rs.next()) {
                ClubModel club = new ClubModel(rs.getInt("club_id"), rs.getString("name"));
                toReturn.add(club);
            }

            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

        return toReturn;
    }

    public static ClubModel getClub(String needle) throws SQLException {
        String queryWhere = "";
        ClubModel club = null;

        // Check if input is a number, else assume its a name
        try{
            int newNeedle = Integer.parseInt(needle);
            queryWhere = "c.club_id = "+newNeedle;
        }
        catch( Exception e){
            queryWhere = "c.name = '"+needle+"'";
        }

        try {
            String query = "SELECT c.club_id, c.name FROM club c WHERE "+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
                club = new ClubModel(rs.getInt("club_id"), rs.getString("name"));
            }

            rs.close();
        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return club;
    }

    public static void addClub(String newClubName) throws SQLException {

        PreparedStatement prepareStatement = null;
        try (Connection db = DbTool.getINSTANCE().dbLoggIn()) {
            ResultSet rs = null;
            String query = "INSERT INTO club (name) VALUES(?)";

            prepareStatement = db.prepareStatement(query);
            prepareStatement.setString(1, newClubName);

            rs = prepareStatement.executeQuery();

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
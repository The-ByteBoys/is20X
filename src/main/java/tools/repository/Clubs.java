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
            queryWhere = "club_id = "+newNeedle;
        }
        catch( Exception e){
            queryWhere = "name = '"+needle+"'";
        }

        try {
            String query = "SELECT c.club_id, c.name, CONCAT(u.fName, ' ', u.lName) owner FROM club c INNER JOIN user u ON c.owner = u.user_id WHERE "+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
                club = new ClubModel(rs.getInt("club_id"), rs.getString("name"));
                club.set(Club.OWNER, rs.getString("owner"));
            }

            rs.close();
        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return club;
    }

    public static void addClub(String newClubName) throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;
        
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "INSERT INTO club (name, owner) VALUES(?,?)";
            prepareStatement = db.prepareStatement(query);
            prepareStatement.setString(1, newClubName);
            prepareStatement.setObject(2, 1); // CURRENTLY LOGGED IN USER

            rs = prepareStatement.executeQuery();

            rs.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
        finally {
            if(db != null){
                db.close();
            }
        }
    }

}
package tools.repository;

// import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import enums.Athlete;
import models.AthleteModel;
// import models.UserModel;
import tools.DbTool;
// import tools.CustomException;

public class Athletes {

    public static List<AthleteModel> getAthletes() throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;

        List<AthleteModel> toReturn = new ArrayList<>();

        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "SELECT a.athlete_id, a.name, a.birth, c.name FROM athlete a INNER JOIN club c ON a.club = c.club_id";
            prepareStatement =  db.prepareStatement(query);
            rs = prepareStatement.executeQuery();

            while (rs.next()) {
                AthleteModel athlete = new AthleteModel(rs.getInt("a.athlete_id"), rs.getString("a.name"), rs.getInt("a.birth"), rs.getString("c.name"));
                toReturn.add(athlete);
            }
            rs.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

        return toReturn;
    }

    // public static void main(String[] args) {
        // System.out.println("Hello world");
    // }

    public static AthleteModel getAthlete(String needle) throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;
        String queryWhere = "";
        AthleteModel athlete = null;

        // Check if input is a number, else assume its a name
        try{
            int newNeedle = Integer.parseInt(needle);
            queryWhere = "a.id = "+newNeedle;
        }
        catch( Exception e){
            queryWhere = "a.name = '"+needle+"'";
        }

        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "SELECT a.athlete_id, a.name, a.birth, c.name FROM athlete a INNER JOIN club c ON a.club = c.club_id WHERE "+queryWhere;
            prepareStatement = db.prepareStatement(query);
            rs = prepareStatement.executeQuery();
            while(rs.next()){
                athlete = new AthleteModel(rs.getInt("a.athlete_id"), rs.getString("a.name"), rs.getInt("a.birth"), rs.getString("c.name"));
            }

            db.close();

        } catch(SQLException e){
            e.printStackTrace();
            throw e;
        }

        return athlete;

    }

    public static void addAthlete(AthleteModel newAthlete, int clubID) throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;

        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "INSERT INTO athlete (name, birth, club) VALUES(?,?,?)";
            prepareStatement = db.prepareStatement(query);
            prepareStatement.setString(1,newAthlete.get(Athlete.NAME).toString());
            prepareStatement.setObject(2,newAthlete.get(Athlete.BIRTH));
            prepareStatement.setInt(3,clubID);
            rs = prepareStatement.executeQuery();
            db.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw e;
        }
    }

}
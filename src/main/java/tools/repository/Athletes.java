package tools.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import enums.Athlete;
import models.AthleteModel;
import tools.DbTool;

public class Athletes {

    private Athletes() {
        throw new IllegalStateException("Utility class");
    }

    public static List<AthleteModel> getAthletes() throws SQLException {
        List<AthleteModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT a.firstName, a.lastName, a.birth, a.sex FROM athlete a";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while (rs.next()) {
                AthleteModel athlete = new AthleteModel(rs.getString("a.firstName"), rs.getString("a.lastName"), rs.getInt("a.birth"), rs.getString("a.sex"));
                toReturn.add(athlete);
            }

            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }

        return toReturn;
    }

    public static AthleteModel getAthlete(String needle) throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;
        String queryWhere = "";
        AthleteModel athlete = null;

        // Check if input is a number, else assume its a name
        try{
            int newNeedle = Integer.parseInt(needle);
            queryWhere = "a.athlete_id = "+newNeedle;
        }
        catch( Exception e){
            queryWhere = "a.name = '"+needle+"'";
        }

        try {
            String query = "SELECT a.firstName, a.lastName, a.birth, a.sex FROM athlete a WHERE "+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
                athlete = new AthleteModel(rs.getString("a.firstName"), rs.getString("a.lastName"), rs.getInt("a.birth"), rs.getString("a.sex"));
            }

            rs.close();

            /** if(athlete != null){
                String query2 = "SELECT c.name FROM (classPeriod p INNER JOIN class c ON p.class = c.class_id ) INNER JOIN athlete a ON p.athlete = a.athlete_id WHERE "+queryWhere;
                rs = DbTool.getINSTANCE().selectQuery(query2);

                while(rs.next()){
                    athlete.addAthleteClub( rs.getString("c.name") );
                }
                rs.close();
            }*/

        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return athlete;
    }

    public static void addAthlete(AthleteModel newAthlete) throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;
        
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "INSERT INTO athlete (firstName, lastName, birth, sex) VALUES(?,?,?,?)";
            prepareStatement = db.prepareStatement(query);

            prepareStatement.setString(1, newAthlete.get(Athlete.FNAME).toString());
            prepareStatement.setString(2, newAthlete.get(Athlete.LNAME).toString());
            prepareStatement.setObject(3, ((int) newAthlete.get(Athlete.BIRTH) > 0)?newAthlete.get(Athlete.BIRTH):null);
            prepareStatement.setString(4,newAthlete.get(Athlete.SEX).toString());

            rs = prepareStatement.executeQuery();

            rs.close();
            db.close();
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
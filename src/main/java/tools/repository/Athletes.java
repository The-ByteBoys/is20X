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
        List<AthleteModel> toReturn = new ArrayList<>();

        try {
            String query = "SELECT a.name, a.birth, c.name, a.sex FROM athlete a INNER JOIN club c ON a.club = c.club_id";

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while (rs.next()) {
                AthleteModel athlete = new AthleteModel(rs.getString("a.name"), rs.getInt("a.birth"), rs.getString("c.name"), rs.getString("a.sex"));
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
            String query = "SELECT a.name, a.birth, c.name, a.sex FROM athlete a INNER JOIN club c ON a.club = c.club_id WHERE "+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
                athlete = new AthleteModel(rs.getString("a.name"), rs.getInt("a.birth"), rs.getString("c.name"), rs.getString("a.sex"));
            }

            rs.close();

            if(athlete != null){
                String query2 = "SELECT c.name FROM (roro.classPeriod p INNER JOIN roro.class c ON p.class = c.class_id ) INNER JOIN roro.athlete a ON p.athlete = a.athlete_id WHERE "+queryWhere;
                rs = DbTool.getINSTANCE().selectQuery(query2);

                while(rs.next()){
                    athlete.setAthleteClass( rs.getString("c.name") );
                }
                rs.close();
            }

        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return athlete;
    }

    public static void addAthlete(AthleteModel newAthlete) throws SQLException {
        addAthlete(newAthlete, 0);
    }

    public static void addAthlete(AthleteModel newAthlete, int clubID) throws SQLException {
        Connection db = null;
        PreparedStatement prepareStatement = null;
        
        try {
            db = DbTool.getINSTANCE().dbLoggIn();
            ResultSet rs = null;
            String query = "INSERT INTO athlete (name, birth, club, sex) VALUES(?,?,?,?)";
            prepareStatement = db.prepareStatement(query);
            prepareStatement.setString(1,newAthlete.get(Athlete.NAME).toString());
            
            if((int) newAthlete.get(Athlete.BIRTH) > 0){
                prepareStatement.setInt(2, (int) newAthlete.get(Athlete.BIRTH));
            }
            else {
                prepareStatement.setObject(2, null);
            }
            
            prepareStatement.setInt(3,clubID);
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
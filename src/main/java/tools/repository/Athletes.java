package tools.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.Athlete;
import enums.User;
import models.AthleteModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author Eirik Svagård
 */
public class Athletes {

    private Athletes() {
        throw new IllegalStateException("Utility class");
    }

    public static List<AthleteModel> getAthletes() throws SQLException {
        return getAthletes(0);
    }

    public static List<AthleteModel> getAthletes(int clubId) throws SQLException {
        List<AthleteModel> toReturn = new ArrayList<>();
        String queryWhere = "";

        if(clubId > 0){
            queryWhere = " INNER JOIN club_reg cr ON a.athlete_id = cr.athlete WHERE cr.club = "+clubId;
        }

        try {
            String query = "SELECT a.firstName, a.lastName, a.birth, a.sex FROM athlete a"+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while (rs.next()) {
                AthleteModel athlete = new AthleteModel(null, rs.getString("a.firstName"), rs.getString("a.lastName"), rs.getDate("a.birth"), rs.getString("a.sex"));
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
        String queryWhere = "";
        AthleteModel athlete = null;

        // Check if input is a number, else assume its a name
        try{
            int newNeedle = Integer.parseInt(needle);
            queryWhere = "a.athlete_id = "+newNeedle;
        }
        catch( Exception e){
            queryWhere = "CONCAT(a.firstName, ' ', a.lastName) = '"+needle+"'";
        }

        try {
            String query = "SELECT a.athlete_id, a.firstName, a.lastName, a.birth, a.sex FROM athlete a WHERE "+queryWhere;

            ResultSet rs = DbTool.getINSTANCE().selectQuery(query);

            while(rs.next()){
                athlete = new AthleteModel(rs.getInt("a.athlete_id"), rs.getString("a.firstName"), rs.getString("a.lastName"), rs.getDate("a.birth"), rs.getString("a.sex"));
            }

            rs.close();
        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return athlete;
    }

    /**
     * Method to add athletes to the datasource. Returns the ID if athlete already exist.
     *
     * @param newAthlete
     * @return athlete ID
     * @throws NamingException
     * @throws SQLException
     */
    public static Integer addAthlete(AthleteModel newAthlete) throws NamingException, SQLException {
        AthleteModel checkIfExistsAthlete = getAthlete(newAthlete.get(Athlete.FNAME)+" "+newAthlete.get(Athlete.LNAME));

        if(checkIfExistsAthlete == null) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("firstName", newAthlete.get(Athlete.FNAME));
            parameters.put("lastName", newAthlete.get(Athlete.LNAME));
            parameters.put("birth", newAthlete.get(Athlete.BIRTH));
            parameters.put("sex", newAthlete.get(Athlete.SEX));

            Context ctx = new InitialContext();
            JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("athlete").usingGeneratedKeyColumns("athlete_id");
            return insert.executeAndReturnKey(parameters).intValue();
        }
        else {
            return (int) checkIfExistsAthlete.get(Athlete.ID);
        }
    }

    /**
     * Add a clubID to an AthleteID for the DataSource
     *
     * @param AthleteID
     * @param clubID
     * @throws NamingException
     */
    public static void addAthleteToClub(int AthleteID, int clubID) throws NamingException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("athlete", AthleteID);
        parameters.put("club", clubID);

        Context ctx = new InitialContext();
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("club_reg");
        insert.execute(parameters);
    }

}
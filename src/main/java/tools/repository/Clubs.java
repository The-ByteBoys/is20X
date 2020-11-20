package tools.repository;

// import java.io.PrintWriter;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import enums.*;
import models.ClubModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Clubs {

    public static List<ClubModel> getClubs() throws SQLException {
        List<ClubModel> toReturn = new ArrayList<>();

        String query = "SELECT club_id, name FROM club;";

        try (ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query)){
            while (rs.next()) {
                ClubModel club = new ClubModel(rs.getInt("club_id"), rs.getString("name"));
                toReturn.add(club);
            }
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

        String query = "SELECT c.club_id, c.name FROM club c WHERE "+queryWhere;
        try (ResultSet rs = DbTool.getINSTANCE().selectQuery(query)){
            while(rs.next()){
                club = new ClubModel(rs.getInt("club_id"), rs.getString("name"));
            }
        }

        return club;
    }

    /**
     * Search for clubs
     * @param name Search-string for a club name
     * @return List of ClubModels matching search
     * @throws SQLException if SQL fails
     */
    public static List<ClubModel> findClubs(String name) throws SQLException {
        List<ClubModel> clubList = new ArrayList<>();

        String needle = "%"+name.toLowerCase().replace(" ", "%")+"%";
        String query = "SELECT c.club_id, c.name FROM club c WHERE c.name LIKE ?";

        try (ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, needle)){

            while(rs.next()){
                ClubModel club = new ClubModel(rs.getInt("club_id"), rs.getString("name"));
                clubList.add(club);
            }

        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return clubList;
    }

    public static int addClub(String newClubName) throws SQLException, NamingException {

        // Check if the club already exists
        ClubModel checkClubExist = getClub(newClubName);
        if(checkClubExist != null){
            return (int) checkClubExist.get(Club.ID);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", newClubName);

        Context ctx = new InitialContext();
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("club").usingGeneratedKeyColumns("club_id");
        return insert.executeAndReturnKey(parameters).intValue();

    }
}
package tools.repository;

// import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.*;
import models.ClubModel;
// import models.UserModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
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
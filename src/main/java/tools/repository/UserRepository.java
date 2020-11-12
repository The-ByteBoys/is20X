package tools.repository;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import models.UserModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;
import enums.*;

import javax.naming.NamingException;

public class UserRepository {

    public static final int TIMEOUT = 30;

    /**
     * Get UserModel from token provided
     * @param token from cookie
     * @return UserModel if not expired, null if it is
     */
    public static UserModel getUserFromToken(String token) {
        UserModel user = null;
        try {
            String query = "SELECT user, created FROM userLogin WHERE loginToken = ?";
            ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, token);
            while (rs.next()) {
//                System.out.println("created: "+rs.getTimestamp("created").getTime()+ "\t timestamp: "+(new Timestamp(Calendar.getInstance().getTime().getTime()).getTime()-(TIMEOUT*60*1000))+"\n");
                if( rs.getTimestamp("created").getTime() > (new Timestamp(Calendar.getInstance().getTime().getTime()).getTime()-(TIMEOUT*60*1000)) ){
                    // EXPIRED TOKEN
                    //TODO: delete token
                    return null;
                }
                else {
                    user = getUserFromId(rs.getInt("user"));
                }
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    /**
     * Gets the UserModel for a provide user_id
     * @param userid userid
     * @return UserModel
     * @throws SQLException if query fails (e.g: userid doesn't exist)
     */
    public static UserModel getUserFromId(int userid) throws SQLException {
        UserModel user = null;

        try {
            String query = "SELECT email, password, userType FROM user WHERE user_id = ? LIMIT 1";

            ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, userid);

            while(rs.next()){
                String userType = rs.getString("userType");
                user = new UserModel(userid, rs.getString("email"), userType, null);
                if(userType.equals(UserLevel.COACH.toString()) || userType.equals(UserLevel.ATHLETE.toString())){

                    query = "SELECT cr.club, a.athlete_id FROM ((club_user cu INNER JOIN athlete a ON cu.athlete = a.athlete_id) INNER JOIN user u ON cu.user = u.user_id) INNER JOIN club_reg cr ON a.athlete_id = cr.athlete WHERE user_id = ? LIMIT 1";
                    ResultSet rs2 = DbTool.getINSTANCE().selectQueryPrepared(query, userid);

                    while(rs2.next()){
                        user.set(User.CLUBID, rs2.getInt("club"));
                        user.set(User.ATHLETEID, rs2.getInt("athlete_id"));
                    }
                    rs2.close();

                }
            }

            rs.close();

        } catch(SQLException | NullPointerException e){
            e.printStackTrace();
            throw e;
        }

        return user;
    }

    /**
     * Save UserToken in database
     * @param userid userid
     * @param token  token used for login
     * @throws NamingException if datasource is not found
     */
    public static void setUserToken(int userid, String token) throws NamingException {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", userid);
        parameters.put("loginToken", token);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DbTool.getINSTANCE().getDataSource());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("userLogin").usingGeneratedKeyColumns("created");
        insert.execute(parameters);
    }

    public static void connectUserAndAthlete(int userid, int athleteid) throws NamingException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user", userid);
        parameters.put("athlete", athleteid);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DbTool.getINSTANCE().getDataSource());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("club_user");
        insert.execute(parameters);
    }
}
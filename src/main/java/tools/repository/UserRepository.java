package tools.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.UserModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.DbTool;
import enums.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UserRepository {

    /**
     * Get UserModel from token provided
     * @param token from cookie
     * @return UserModel if not expired, null if it is
     */
    public static UserModel getUserFromToken(String token) {
        UserModel user = null;
        try {
            String query = "SELECT user FROM userLogin WHERE loginToken = ?";
            ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, token);
            while (rs.next()) {
                user = getUserFromId(rs.getInt("user"));
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
                if(userType.equals(UserLevel.COACH.toString())){

                    query = "SELECT cr.club FROM ((club_user cu INNER JOIN athlete a ON cu.athlete = a.athlete_id) INNER JOIN user u ON cu.user = u.user_id) INNER JOIN club_reg cr ON a.athlete_id = cr.athlete WHERE user_id = ? LIMIT 1";
                    ResultSet rs2 = DbTool.getINSTANCE().selectQueryPrepared(query, userid);

                    while(rs2.next()){
                        user.set(User.CLUBID, rs2.getInt("club"));
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

        Context ctx = new InitialContext();
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("userLogin").usingGeneratedKeyColumns("created");
        insert.execute(parameters);
    }
}
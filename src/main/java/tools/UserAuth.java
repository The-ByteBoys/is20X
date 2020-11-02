package tools;

import enums.*;
import models.UserModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import tools.repository.UserRepository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserAuth {

    private static String lagToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[10];
        random.nextBytes(bytes);
        StringBuilder returnToken = new StringBuilder();

        for(byte bit : bytes){
            returnToken.append( String.format("%x", bit));
        }

        return returnToken.toString();
    }

    private static final String PASSWORD_SECRET = "FrityrstektSnitzel";

    private static String getKrypterPassord(String passord) {
        return DigestUtils.md5Hex(passord + PASSWORD_SECRET).toUpperCase();
    }

    public static Integer opprettBruker(UserModel bruker) throws NamingException {

        String passord = getKrypterPassord(bruker.get(User.PASSWORD).toString());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", bruker.get(User.EMAIL));
        parameters.put("password", passord);
        parameters.put("userType", bruker.get(User.TYPE));

        Context ctx = new InitialContext();
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("user").usingGeneratedKeyColumns("user_id");
        return insert.executeAndReturnKey(parameters).intValue();
    }

    public static String checkLogin(String username, String password) throws SQLException {
        String toReturn = null;

        String query = "SELECT user_id, email FROM user WHERE email = ? AND password = ?";
        ResultSet rs = DbTool.getINSTANCE().selectQueryPrepared(query, username, getKrypterPassord(password));

        while(rs.next()){
            if(rs.getString("email").equals(username)) {
                toReturn = lagToken();

                try {
                    UserRepository.setUserToken(rs.getInt("user_id"), toReturn);
                } catch (NamingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        rs.close();

        return toReturn;
    }

    public static UserModel verifyLogin(HttpServletRequest request) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                String name = ck.getName();
                String value = ck.getValue();
                if (name.equals("auth")) {
                    return UserRepository.getUserFromToken(value);
                }
            }
        }

        return null;
    }

    public static UserModel requireLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return requireLogin(request, response, UserLevel.ATHLETE);
    }

    public static UserModel requireLogin(HttpServletRequest request, HttpServletResponse response, UserLevel level) throws IOException {
        UserModel user = null;
        user = verifyLogin(request);

        if( user == null ){
            response.sendRedirect("login.jsp");
            return null;
        }

        if(!checkUserPerm(user, level)){
            HttpSession session = request.getSession();
            session.setAttribute("error", "You are not allowed to visit that page.");
            response.sendRedirect("mypage.jsp");
            return null;
        }

        return user;
    }

    public static boolean checkUserPerm(UserModel user, UserLevel level){

        return (level.equals(UserLevel.ATHLETE)) ||
                (level.equals(UserLevel.COACH) && (user.get(User.TYPE).equals(UserLevel.COACH.toString()) || user.get(User.TYPE).equals(UserLevel.ADMIN.toString()))) ||
                (level.equals(UserLevel.ADMIN) && user.get(User.TYPE).equals(UserLevel.ADMIN.toString()));
    }

    public static String navBarLogin(UserModel user){
        StringBuilder string = new StringBuilder();

        if(user != null){
            string.append("<div id=\"loginInfo\" style=\"display: none;\">");
            string.append("<a class=\"nav-item nav-link\" id='login' href=\"mypage.jsp\">Min side</a>");
            string.append("</div>\n");
        }

        return string.toString();
    }

    public static String getSessionNotes(HttpSession session){
        StringBuilder toreturn = new StringBuilder();

        Object error;
        if((error = session.getAttribute("error")) != null){
            toreturn.append("<p class='alert alert-danger' style='max-width: 350px; margin: auto;'>");
            toreturn.append(error);
            toreturn.append("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>");
            toreturn.append("</p>");
            toreturn.append("<br>");
            session.removeAttribute("error");
        }

        Object msg;
        if((msg = session.getAttribute("msg")) != null){
            toreturn.append("<p class='alert alert-success' style='max-width: 350px; margin: auto;'>");
            toreturn.append(msg);
            toreturn.append("<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>");
            toreturn.append("</p>");
            toreturn.append("<br>");
            session.removeAttribute("msg");
        }

        return toreturn.toString();
    }
}

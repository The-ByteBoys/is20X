package tools;

import enums.User;
import models.UserModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class PasswordEncrypt {

    private String lagToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return bytes.toString();
    }

    private static String PASSWORD_SECRET = "FrityrstektSnitzel";

    private static String getKrypterPassord(String passord) {
        return DigestUtils.md5Hex(passord + PASSWORD_SECRET).toUpperCase();
    }

    public static Integer opprettBruker(UserModel bruker) throws NamingException {

        String passord = getKrypterPassord(bruker.get(User.PASSWORD).toString());
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("fName", bruker.get(User.FNAME));
        parameters.put("lName", bruker.get(User.LNAME));
        parameters.put("email", bruker.get(User.EMAIL));
        parameters.put("pass", passord);

        Context ctx = new InitialContext();
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource) ctx.lookup("roingdb"));

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("user").usingGeneratedKeyColumns("user_id");
        return insert.executeAndReturnKey(parameters).intValue();
    }
}
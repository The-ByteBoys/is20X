package tools;

import enums.User;
import models.UserModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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
    "
    private String getKrypterPassord(String passord) {
        return DigestUtils.md5Hex(passord + PASSWORD_SECRET).toUpperCase();
    }

    public Integer opprettBruker(UserModel bruker) {

        String passord = getKrypterPassord(bruker.get(User.PASSWORD).toString());
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("passord", passord);


        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("bruker").usingGeneratedKeyColumns("id");
        return insert.executeAndReturnKey(parameters).intValue();
    }
}
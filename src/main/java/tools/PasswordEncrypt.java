package tools;

import models.UserModel;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import java.util.HashMap;
import java.util.Map;

public class PasswordEncrypt {
    public Integer opprettBruker(UserModel bruker) {

        String passord = getKrypterPassord(bruker.getPassord());
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("passord", passord);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("bruker").usingGeneratedKeyColumns("id");
        return insert.executeAndReturnKey(parameters).intValue();
    }
}
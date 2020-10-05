package tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
// import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class DbTool {
    private static final tools.DbTool INSTANCE = new tools.DbTool();
    static Connection connection;

    /**
     * initiates the class as a singleton.
     *
     * @return DbTool
     */
    public static tools.DbTool getINSTANCE() {
        return INSTANCE;
    }

    private static Map<String, String> getProperties() {
        Map<String, String> result = new HashMap<>();
        try (InputStream input = new FileInputStream("/opt/payara/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            result.put("username", prop.getProperty("username"));
            result.put("password", prop.getProperty("password"));
            result.put("URL", prop.getProperty("URL"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;

    }

//    public static void main(String[] args) {
//         Map<String, String> map = getProperties();
//         for(String s: map.keySet()) {
//             System.out.println(map.get("URL"));
//         }
//    }

    /**
     * Establishes a connection with a mariaDB or returns an existing one.
     *
     * @param out for html printing in front-end e.g. (for errors or content)
     * @return connection to db
     * @throws SQLException if the connection fails
     */
    public Connection dbLoggIn() throws SQLException {
        Connection toReturn = null;
        Map<String, String> result = getProperties();

        try {
            toReturn = (connection != null)
                ? connection
                : DriverManager.getConnection(
                    result.get("URL"),
                    result.get("username"),
                    result.get("password"));

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
            // out.println("SQL Exception " + e);
        }
        return toReturn;
    }


    /**
     * Executes 'query' and returns the ResultSet of the request.
     * 
     * @param query
     * @return ResultSet from query
     * @throws SQLException
     */
    public ResultSet selectQuery(String query) throws SQLException {
        Connection db = null;
        PreparedStatement statement = null;

        db = this.dbLoggIn();

        ResultSet rs = null;
        statement = db.prepareStatement(query);
        rs = statement.executeQuery();
        
        return rs;
    }
}


package tools;

import enums.Result;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
// import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class DbTool {
    private static final tools.DbTool INSTANCE = new tools.DbTool();
    static Connection connection;
    private DataSource dataSource;

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


    /**
     * Establishes a connection with a mariaDB or returns an existing one.
     *
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
     * REMEMBER to close the ResultSet!
     * 
     * @param query SQL query
     * @return ResultSet from query
     * @throws SQLException if query fails
     */
    public ResultSet selectQuery(String query) throws SQLException {
        Connection db = null;
        PreparedStatement statement = null;

        db = this.dbLoggIn();

        ResultSet rs = null;
        statement = db.prepareStatement(query);
        rs = statement.executeQuery();

        db.close();
        
        return rs;
    }

    public ResultSet selectQueryPrepared(String query, Object... args) throws SQLException {
        Connection db = null;
        PreparedStatement statement = null;

        db = this.dbLoggIn();

        ResultSet rs = null;
        statement = db.prepareStatement(query);
        int argCounter = 1;
        for (Object arg : args){
            statement.setString(argCounter, arg.toString());
            argCounter++;
        }

        rs = statement.executeQuery();

        db.close();

        return rs;
    }

    public DataSource getDataSource() throws NamingException {
        if(dataSource == null){
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("roingdb");
        }
        return dataSource;
    }
}

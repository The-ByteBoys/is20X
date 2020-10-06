package tools;
import models.UserModel;

import javax.xml.registry.infomodel.User;
import java.sql.*;

public class UserDAO {

    public User checkLogin(String username, String password) throws SQLException,
            ClassNotFoundException {
        String jdbcURL = "jdbc:mysql://localhost:3306/";
        String dbUser = "root";
        String dbPassword = "eplepai";

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        String sql = "SELECT * FROM users WHERE username = ? and password = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();

        UserDAO UserDAO = null;

        connection.close();

        return (User) null;
    }
}
package databaser.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public abstract class ActiveDomainObject {
    public static final String dbUsername = "root",
            dbPassword = "yolopass",
            dbURL = "jdbc:mysql://localhost:3306/mydb";


    public abstract void save();

    protected static Connection getConnection() throws SQLException {
        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbUsername);
        connectionProps.put("password", dbPassword);

        conn = DriverManager.getConnection(dbURL, connectionProps);
        System.out.println("Connected to database");
        return conn;
    }

    protected static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}

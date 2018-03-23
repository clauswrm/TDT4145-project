package databaser.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;


/**
 * An abstract base class which all classes that represents an entity in the database should inherrit from.
 */
public abstract class ActiveDomainObject {
    public static String dbUsername = "root",
            dbPassword = "yolopass",
            dbURL = "jdbc:mysql://localhost:3306/mydb?useSSL=false";


    /**
     * Attempts to save the objects state to the given {@link #dbURL database URL}.
     *
     * @throws RuntimeException if an error occured trying to save to the database.
     */
    public abstract void save();


    /**
     * Attempts to establishes a connection to a database given the {@link #dbURL},
     * {@link #dbUsername} and {@link #dbPassword} set in the {@link ActiveDomainObject} class.
     *
     * @return a connection to the database.
     * @throws SQLException if an error occured trying to establish a connection.
     * @see Connection
     */
    protected static Connection getConnection() throws SQLException {
        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbUsername);
        connectionProps.put("password", dbPassword);

        conn = DriverManager.getConnection(dbURL, connectionProps);
        return conn;
    }


    /**
     * Helper method for all implementations of {@link ActiveDomainObject}.
     * Sets the parameters in a {@link PreparedStatement}.
     *
     * @param statement  the statement the parameters should be inserted into.
     * @param parameters the parameters to be inserted into the statement.
     * @throws SQLException if the given parameters does not match the parameters in the statement.
     * @see PreparedStatement
     */
    protected static void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            // Parameters are 1-indexed
            statement.setObject(i + 1, parameters[i]);
        }
    }
}

package databaser.persistence;

import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an Apparat in the database.
 *
 * @see ActiveDomainObject
 */
@Data
public class Apparat extends ActiveDomainObject implements Comparable<Apparat> {

    private int apparatID;
    private String navn;
    private String beskrivelse;

    public Apparat(int apparatID, String navn, String beskrivelse) {
        this.apparatID = apparatID;
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public Apparat(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    @Override
    public void save() {
        if (this.navn == null || this.beskrivelse == null) {
            throw new IllegalArgumentException("Navn and beskrivelse must be set");
        }

        final String sql = "INSERT INTO apparat (Navn, Beskrivelse) VALUES (?, ?)";
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            setParameters(statement, navn, beskrivelse);
            statement.execute();

            // Updates the apparatID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setApparatID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Apparat=" + navn + " to database", e);
        }
    }

    /**
     * Loads an Apparat with the given apparatID from the database.
     *
     * @param apparatID the ID of the Apparat to be loaded from the database.
     * @return the Apparat in the database with corresponding apparatID.
     * @throws RuntimeException if the given apparatID was not found, or if connecting to the database failed.
     */
    public static Apparat getApparatFromID(int apparatID) {
        final String sql = "SELECT * FROM apparat WHERE apparatID = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, apparatID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String navn = resultSet.getString("Navn");
            String beskrivelse = resultSet.getString("Beskrivelse");

            return new Apparat(apparatID, navn, beskrivelse);

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Apparat with id=" + apparatID + " from the database", e);
        }
    }

    /**
     * Loads all Apparat from the database.
     *
     * @return a list of all Apparat in the database, sorted alphabetically by name.
     * @throws RuntimeException if connecting to the database failed.
     */
    public static List<Apparat> getAll() {
        final String sql = "SELECT * FROM apparat";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Apparat> results = new ArrayList<>();

            while (resultSet.next()) {
                int apparatID = resultSet.getInt("apparatID");
                String navn = resultSet.getString("Navn");
                String beskrivelse = resultSet.getString("Beskrivelse");

                results.add(new Apparat(apparatID, navn, beskrivelse));
            }
            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Apparat from the database", e);
        }
    }

    @Override
    public int compareTo(Apparat other) {
        return this.getNavn().compareTo(other.getNavn());
    }
    public String toString(){return navn;}
}

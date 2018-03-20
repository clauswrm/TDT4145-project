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
 * Represents an Øvelsesgruppe in the database.
 *
 * @see ActiveDomainObject
 */
@Data
public class Øvelsesgruppe extends ActiveDomainObject implements Comparable<Øvelsesgruppe> {

    private int øvelsesgruppeID;
    private String navn;

    public Øvelsesgruppe(int øvelsesgruppeID, String navn) {
        this.øvelsesgruppeID = øvelsesgruppeID;
        this.navn = navn;
    }

    public Øvelsesgruppe(String navn) {
        this.navn = navn;
    }

    @Override
    public void save() {
        if (this.navn == null) {
            throw new IllegalArgumentException("Navn must be set");
        }

        final String sql = "INSERT INTO øvelsegruppe (Navn) VALUES (?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            setParameters(statement, navn);
            statement.execute();

            // Updates the øvelsesgruppeID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setØvelsesgruppeID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Øvelsesgruppe=" + navn + " to database", e);
        }
    }


    /**
     * Fetches all {@link Øvelse} in the Øvelsesgruppe from the database.
     *
     * @return a list of all øvelser in the Øvelsesgruppe.
     * @throws RuntimeException if connecting to the database failed.
     */
    public List<Øvelse> getØvelser() {
        final String sql_friøvelse = "SELECT * FROM (friøvelse_has_øvelsegruppe NATURAL JOIN friøvelse) " +
                "WHERE idØvelsegruppe = ?";
        final String sql_apparatøvelse = "SELECT * FROM (apparatøvelse_has_øvelsegruppe NATURAL JOIN apparatøvelse) " +
                "WHERE idØvelsegruppe = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement_friøvelse = connection.prepareStatement(sql_friøvelse);
                PreparedStatement statement_apparatøvelse = connection.prepareStatement(sql_apparatøvelse);
        ) {

            setParameters(statement_friøvelse, øvelsesgruppeID);
            setParameters(statement_apparatøvelse, øvelsesgruppeID);
            ResultSet resultSet_friøvelse = statement_friøvelse.executeQuery();
            ResultSet resultSet_apparatøvelse = statement_apparatøvelse.executeQuery();

            List<Øvelse> øvelser = new ArrayList<>();
            øvelser.addAll(Friøvelse.getFriøvelserFromResultSet(resultSet_friøvelse));
            øvelser.addAll(Apparatøvelse.getApparatøvelserFromResultSet(resultSet_apparatøvelse));

            Collections.sort(øvelser);
            return øvelser;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Øvelser from Øvelsesgruppe with id=" + øvelsesgruppeID + " from the database", e);
        }
    }

    /**
     * Loads an Øvelsesgruppe with the given øvelsesgruppeID from the database.
     *
     * @param øvelsesgruppeID the ID of the Øvelsesgruppe to be loaded from the database.
     * @return the Øvelsesgruppe in the database with corresponding øvelsesgruppeID.
     * @throws RuntimeException if the given øvelsesgruppeID was not found, or if connecting to the database failed.
     */
    public static Øvelsesgruppe getØvelsesgruppeFromID(int øvelsesgruppeID) {
        final String sql = "SELECT * FROM øvelsegruppe WHERE idØvelsegruppe=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, øvelsesgruppeID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String navn = resultSet.getString("Navn");

            return new Øvelsesgruppe(øvelsesgruppeID, navn);

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Øvelsesgruppe with id=" + øvelsesgruppeID + " from the database", e);
        }
    }

    /**
     * Loads all Øvelsesgruppe from the database.
     *
     * @return a list of all Øvelsesgruppe in the database, sorted alphabetically by name.
     * @throws RuntimeException if connecting to the database failed.
     */
    public static List<Øvelsesgruppe> getAll() {
        final String sql = "SELECT * FROM øvelsegruppe";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Øvelsesgruppe> results = new ArrayList<>();

            while (resultSet.next()) {
                int øvelsesgruppeID = resultSet.getInt("idØvelsegruppe");
                String navn = resultSet.getString("Navn");

                results.add(new Øvelsesgruppe(øvelsesgruppeID, navn));
            }
            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Øvelsesgruppe from the database", e);
        }
    }

    @Override
    public int compareTo(Øvelsesgruppe other) {
        return this.getNavn().compareTo(other.getNavn());
    }
}

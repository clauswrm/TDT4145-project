package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Friøvelse in the database.
 *
 * @see ActiveDomainObject
 */
public class Friøvelse extends Øvelse {

    public Friøvelse(int øvelseID, String navn, String beskrivelse) {
        super(øvelseID, navn, beskrivelse);
    }

    public Friøvelse(String navn, String beskrivelse) {
        super(navn, beskrivelse);
    }

    @Override
    public void save() {
        if (this.navn == null || this.beskrivelse == null) {
            throw new IllegalArgumentException("Navn and beskrivelse must be set");
        }

        final String sql = "INSERT INTO friøvelse (Navn, Beskrivelse)" +
                "VALUES (?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            setParameters(statement, navn, beskrivelse);
            statement.execute();

            // Updates the øvelseID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setØvelseID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Friøvelse=" + navn + " to the database", e);
        }
    }

    /**
     * Loads a Friøvelse with the given øvelseID from the database.
     *
     * @param øvelseID the ID of the Friøvelse to be loaded from the database.
     * @return the Friøvelse in the database with corresponding øvelseID.
     * @throws RuntimeException if the given øvelseID was not found, or if connecting to the database failed.
     */
    public static Friøvelse getFriøvelseFromID(int øvelseID) {
        final String sql = "SELECT * FROM friøvelse WHERE idFriøvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, øvelseID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String navn = resultSet.getString("Navn");
            String beskrivelse = resultSet.getString("Beskrivelse");

            return new Friøvelse(øvelseID, navn, beskrivelse);

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Friøvelse with id=" + øvelseID + " from the database", e);
        }
    }

    /**
     * Loads all Friøvelse from the database.
     *
     * @return a list of all Friøvelse in the database, sorted alphabetically by name.
     * @throws RuntimeException if connecting to the database failed.
     */
    public static List<Friøvelse> getAll() {
        final String sql = "SELECT * FROM friøvelse";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Friøvelse> results = getFriøvelserFromResultSet(resultSet);

            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Friøvelse from the database", e);
        }
    }

    /**
     * Helper method for extracting Friøvelser from a {@link ResultSet}.
     *
     * @param resultSet the ResultSet to get Friøvelser from.
     * @return all Friøvelser in the ResultSet.
     * @throws SQLException if the ResultSet does not contain valid Friøvelser.
     */
    protected static List<Friøvelse> getFriøvelserFromResultSet(ResultSet resultSet) throws SQLException {
        List<Friøvelse> results = new ArrayList<>();

        while (resultSet.next()) {
            int øvelseID = resultSet.getInt("idFriøvelse");
            String navn = resultSet.getString("Navn");
            String beskrivelse = resultSet.getString("Beskrivelse");

            results.add(new Friøvelse(øvelseID, navn, beskrivelse));
        }
        return results;
    }


    /**
     * Adds this Friøvelse to the given {@link Øvelsesgruppe}.
     *
     * @param øvelsesgruppe the Øvelsesgruppe to add the Friøvelse to.
     * @throws RuntimeException if adding the relation to the database failed.
     * @see Øvelsesgruppe
     */
    @Override
    public void addToØvelsesgruppe(Øvelsesgruppe øvelsesgruppe) {
        final String sql = "INSERT INTO friøvelse_has_øvelsegruppe (idFriøvelse, idØvelsegruppe) VALUES (?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            setParameters(statement, øvelseID, øvelsesgruppe.getØvelsesgruppeID());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add øvelse=" + navn + " to øvelsesgruppe=" + øvelsesgruppe.getNavn(), e);
        }
    }


    /**
     * Fetches all {@link Treningsøkt Treningsøkter} that contains this Friøvelse from the database.
     *
     * @return a list of all Treningsøkter that contains this Friøvelse, sorted by date.
     * @see Treningsøkt
     */
    @Override
    public List<Treningsøkt> getTreningsøkterWithØvelse() {
        final String sql = "SELECT (t.idTreningsøkt, t.Dato, t.Varighet, t.Form, t.Innsats) " +
                "FROM (treningsøkt AS t NATURAL JOIN treningsøkt_has_friøvelse) NATURAL JOIN friøvelse AS f " +
                "WHERE f.idFriøvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, øvelseID);
            ResultSet resultSet = statement.executeQuery();
            List<Treningsøkt> results = Treningsøkt.getTreningsøkterFromResultSet(resultSet);

            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to get Treningsøkter with Friøvelse from the database", e);
        }
    }

    public Map<Treningsøkt, String> getProgressForFriøvelse() {
        Map<Treningsøkt, String> progress = new HashMap<>();

        final String sql = "SELECT (t.idTreningsøkt, t.Dato, t.Varighet, t.Form, t.Innsats, x.Beskrivelse) " +
                "FROM (treningsøkt AS t NATURAL JOIN treningsøkt_has_friøvelse AS x) NATURAL JOIN friøvelse AS f " +
                "WHERE f.idFriøvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, øvelseID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int treningsøktID = resultSet.getInt("idtreningsøkt");
                Date dato = resultSet.getDate("dato");
                int varighet = resultSet.getInt("varighet");
                int form = resultSet.getInt("form");
                int innsats = resultSet.getInt("innsats");
                String beskrivelse = resultSet.getString("beskrivelse");

                progress.put(new Treningsøkt(treningsøktID, dato, varighet, form, innsats), beskrivelse);
            }
            return progress;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to get progress for Friøvelse from the database", e);
        }
    }

    @Override
    public String toString() {
        return "Friøvelse{" +
                "øvelseID=" + øvelseID +
                ", navn='" + navn + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                '}';
    }
}

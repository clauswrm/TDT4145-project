package databaser.persistence;

import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an Apparatøvelse in the database.
 *
 * @see ActiveDomainObject
 */
@Data
public class Apparatøvelse extends Øvelse {

    private Apparat apparat;

    public Apparatøvelse(int øvelseID, String navn, String beskrivelse, Apparat apparat) {
        super(øvelseID, navn, beskrivelse);
        this.apparat = apparat;
    }

    public Apparatøvelse(String navn, String beskrivelse, Apparat apparat) {
        super(navn, beskrivelse);
        this.apparat = apparat;
    }

    @Override
    public void save() {
        if (this.navn == null || this.beskrivelse == null) {
            throw new IllegalArgumentException("Navn, apparat and beskrivelse must be set");
        }

        final String sql = "INSERT INTO apparatøvelse (Navn, apparatID, Beskrivelse)" +
                "VALUES (?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            setParameters(statement, navn, apparat.getApparatID(), beskrivelse);
            statement.execute();

            // Updates the øvelseID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setØvelseID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Apparatøvelse=" + navn + " to the database", e);
        }
    }

    /**
     * Loads an Apparatøvelse with the given øvelseID from the database.
     *
     * @param øvelseID the ID of the Apparatøvelse to be loaded from the database.
     * @return the Apparatøvelse in the database with corresponding øvelseID.
     * @throws RuntimeException if the given øvelseID was not found, or if connecting to the database failed.
     */
    public static Apparatøvelse getApparatøvelseFromID(int øvelseID) {
        final String sql = "SELECT * FROM apparatøvelse WHERE idApparatØvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, øvelseID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String navn = resultSet.getString("Navn");
            int apparatID = resultSet.getInt("apparatID");
            String beskrivelse = resultSet.getString("Beskrivelse");

            return new Apparatøvelse(øvelseID, navn, beskrivelse, Apparat.getApparatFromID(apparatID));

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Apparatøvelse with id=" + øvelseID + " from the database", e);
        }
    }

    /**
     * Loads all Apparatøvelse from the database.
     *
     * @return a list of all Apparatøvelse in the database, sorted alphabetically by name.
     * @throws RuntimeException if connecting to the database failed.
     */
    public static List<Apparatøvelse> getAll() {
        final String sql = "SELECT * FROM apparatøvelse";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Apparatøvelse> results = getApparatøvelserFromResultSet(resultSet);

            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Apparatøvelse from the database", e);
        }
    }

    /**
     * Helper method for extracting Apparatøvelser from a {@link ResultSet}.
     *
     * @param resultSet the ResultSet to get Apparatøvelser from.
     * @return all Apparatøvelser in the ResultSet.
     * @throws SQLException if the ResultSet does not contain valid Apparatøvelser.
     */
    protected static List<Apparatøvelse> getApparatøvelserFromResultSet(ResultSet resultSet) throws SQLException {
        List<Apparatøvelse> results = new ArrayList<>();

        while (resultSet.next()) {
            int øvelseID = resultSet.getInt("idApparatØvelse");
            String navn = resultSet.getString("Navn");
            int apparatID = resultSet.getInt("apparatID");
            String beskrivelse = resultSet.getString("Beskrivelse");

            results.add(new Apparatøvelse(øvelseID, navn, beskrivelse, Apparat.getApparatFromID(apparatID)));
        }
        return results;
    }

    /**
     * Adds this Apparatøvelse to the given {@link Øvelsesgruppe}.
     *
     * @param øvelsesgruppe the Øvelsesgruppe to add the Apparatøvelse to.
     * @throws RuntimeException if adding the relation to the database failed.
     * @see Øvelsesgruppe
     */
    @Override
    public void addToØvelsesgruppe(Øvelsesgruppe øvelsesgruppe) {
        final String sql = "INSERT INTO apparatøvelse_has_øvelsegruppe (idApparatØvelse, idØvelsegruppe) VALUES (?, ?)";

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
     * Fetches all {@link Treningsøkt Treningsøkter} that contains this Apparatøvelse from the database.
     *
     * @return a list of all Treningsøkter that contains this Apparatøvelse, sorted by date.
     * @see Treningsøkt
     */
    @Override
    public List<Treningsøkt> getTreningsøkterWithØvelse() {
        final String sql = "SELECT * FROM (treningsøkt NATURAL JOIN treningsøkt_has_apparatøvelse) " +
                "WHERE idApparatØvelse = ?";

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
            throw new RuntimeException("Unable to get Treningsøkter with Apparatøvelse from the database", e);
        }
    }

    public Map<Treningsøkt, Map<String, Integer>> getProgressForApparatøvelse() {
        Map<Treningsøkt, Map<String, Integer>> progress = new HashMap<>();

        final String sql = "SELECT * FROM (treningsøkt NATURAL JOIN treningsøkt_has_apparatøvelse) " +
                "WHERE idApparatØvelse = ?";

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
                int kilo = resultSet.getInt("kilo");
                int reps = resultSet.getInt("reps");
                int set = resultSet.getInt("set");

                Map<String, Integer> dataPoint = new HashMap<>();
                dataPoint.put("kilo", kilo);
                dataPoint.put("reps", reps);
                dataPoint.put("set", set);

                progress.put(new Treningsøkt(treningsøktID, dato, varighet, form, innsats), dataPoint);
            }
            return progress;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to get progress for Apparatøvelse from the database", e);
        }
    }
    public String toString(){
        return navn+"\nBeskrivelse: "+beskrivelse+"\nApparat: "+apparat.getNavn();
    }
}
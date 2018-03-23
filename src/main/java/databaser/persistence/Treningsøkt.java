package databaser.persistence;

import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Treningsøkt in the database.
 *
 * @see ActiveDomainObject
 */
@Data
public class Treningsøkt extends ActiveDomainObject implements Comparable<Treningsøkt> {

    private int treningsøktID;
    private Date dato;
    private int varighet;
    private int form;
    private int innsats;

    public Treningsøkt(int treningsøktID, Date dato, int varighet, int form, int innsats) {
        this.treningsøktID = treningsøktID;
        this.dato = dato;
        this.varighet = varighet;
        this.form = form;
        this.innsats = innsats;
    }

    public Treningsøkt(Date dato, int varighet, int form, int innsats) {
        this.dato = dato;
        this.varighet = varighet;
        this.form = form;
        this.innsats = innsats;
    }

    @Override
    public void save() {
        if (this.dato == null || this.varighet == 0) {
            throw new IllegalArgumentException("Date and varighet must be set");
        }
        final String sql = "INSERT INTO treningsøkt (dato, varighet, form, innsats) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            setParameters(statement, dato, varighet, form, innsats);
            statement.execute();

            // Updates the treningsøktID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setTreningsøktID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Treningsøkt=" + dato + " to database", e);
        }
    }


    /**
     * Fetches all {@link Notat} about this Treningsøkt from the database.
     *
     * @return a list of all Notat about this Treningsøkt.
     * @throws RuntimeException if connecting to the database failed.
     * @see Notat
     */
    public List<Notat> getNotater() {
        final String sql = "SELECT * FROM notat WHERE idTreningsøkt = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID);
            ResultSet resultSet = statement.executeQuery();
            List<Notat> results = new ArrayList<>();

            while (resultSet.next()) {
                int notatID = resultSet.getInt("idNotat");
                String tekst = resultSet.getString("Tekst");

                results.add(new Notat(notatID, tekst, this));
            }
            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Notater about Treningsøkt from the database", e);
        }
    }

    /**
     * Loads a Treningsøkt with the given treningsøktID from the database.
     *
     * @param treningsøktID the ID of the Treningsøkt to be loaded from the database.
     * @return the Treningsøkt in the database with corresponding treningsøktID.
     * @throws RuntimeException if the given treningsøktID was not found, or if connecting to the database failed.
     */
    public static Treningsøkt getTreningsøktFromID(int treningsøktID) {
        final String sql = "SELECT * FROM treningsøkt WHERE idtreningsøkt=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Date dato = resultSet.getDate("dato");
            int varighet = resultSet.getInt("varighet");
            int form = resultSet.getInt("form");
            int innsats = resultSet.getInt("innsats");

            return new Treningsøkt(treningsøktID, dato, varighet, form, innsats);

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Treningsøkt with id=" + treningsøktID + " from the database", e);
        }
    }

    /**
     * Loads all Treningsøkt from the database.
     *
     * @return a list of all Treningsøkt in the database, sorted chronologically.
     * @throws RuntimeException if connecting to the database failed.
     */
    public static List<Treningsøkt> getAll() {
        final String sql = "SELECT * FROM treningsøkt";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Treningsøkt> results = getTreningsøkterFromResultSet(resultSet);
            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Treningsøkt from the database", e);
        }
    }

    /**
     * Helper method for extracting Treningsøkter from a {@link ResultSet}.
     *
     * @param resultSet the ResultSet to get Treningsøkter from.
     * @return all Treningsøkter in the ResultSet.
     * @throws SQLException if the ResultSet does not contain valid Treningsøkter.
     */
    protected static List<Treningsøkt> getTreningsøkterFromResultSet(ResultSet resultSet) throws SQLException {
        List<Treningsøkt> results = new ArrayList<>();

        while (resultSet.next()) {
            int treningsøktID = resultSet.getInt("idtreningsøkt");
            Date dato = resultSet.getDate("dato");
            int varighet = resultSet.getInt("varighet");
            int form = resultSet.getInt("form");
            int innsats = resultSet.getInt("innsats");

            results.add(new Treningsøkt(treningsøktID, dato, varighet, form, innsats));
        }
        return results;
    }

    /**
     * Adds the given {@link Apparatøvelse} to this Treningsøkt.
     *
     * @param apparatøvelse the Apparatøvelse to add to the Treningsøkt.
     * @param kilo          the amount of kilos.
     * @param reps          the amount of kilos.
     * @param set           the amount of kilos.
     * @throws RuntimeException if the given Treningsøkt or Apparatøvelse was not found in the database.
     * @see Apparatøvelse
     */
    public void addApparatøvelse(Apparatøvelse apparatøvelse, int kilo, int reps, int set) {
        final String sql = "INSERT INTO treningsøkt_has_apparatøvelse (idTreningsøkt, idapparatøvelse," +
                "Kilo, Reps, `Set`) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID, apparatøvelse.getØvelseID(), kilo, reps, set);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add øvelse=" + apparatøvelse.getNavn() + " to treningsøkt", e);
        }
    }

    /**
     * Adds the given {@link Friøvelse} to this Treningsøkt.
     *
     * @param friøvelse   the Friøvelse to add to the Treningsøkt.
     * @param beskrivelse the description of how to do the Friøvelse in the Treningsøkt.
     * @throws RuntimeException if the given Treningsøkt or Friøvelse was not found in the database.
     * @see Friøvelse
     */
    public void addFriøvelse(Friøvelse friøvelse, String beskrivelse) {
        final String sql = "INSERT INTO treningsøkt_has_friøvelse (idTreningsøkt, idfriøvelse, Beskrivelse)" +
                "VALUES (?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            setParameters(statement, treningsøktID, friøvelse.getØvelseID(), beskrivelse);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add øvelse=" + friøvelse.getNavn() + " to treningsøkt", e);
        }
    }

    /**
     * Fetches all {@link Øvelse} in the Treningsøkt from the database.
     *
     * @return a list of all øvelser in the Treningsøkt.
     * @throws RuntimeException if connecting to the database failed.
     */
    public List<Øvelse> getØvelser() {
        final String sql_friøvelse = "SELECT friøvelse.idFriøvelse, friøvelse.Navn, friøvelse.Beskrivelse " +
                "FROM (treningsøkt_has_friøvelse JOIN friøvelse " +
                "ON treningsøkt_has_friøvelse.idFriøvelse = friøvelse.idFriøvelse) WHERE idTreningsøkt = ?";
        final String sql_apparatøvelse = "SELECT * FROM (treningsøkt_has_apparatøvelse NATURAL JOIN apparatøvelse) " +
                "WHERE idTreningsøkt = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement_friøvelse = connection.prepareStatement(sql_friøvelse);
                PreparedStatement statement_apparatøvelse = connection.prepareStatement(sql_apparatøvelse);
        ) {

            setParameters(statement_friøvelse, treningsøktID);
            setParameters(statement_apparatøvelse, treningsøktID);
            ResultSet resultSet_friøvelse = statement_friøvelse.executeQuery();
            ResultSet resultSet_apparatøvelse = statement_apparatøvelse.executeQuery();

            List<Øvelse> øvelser = new ArrayList<>();
            øvelser.addAll(Friøvelse.getFriøvelserFromResultSet(resultSet_friøvelse));
            øvelser.addAll(Apparatøvelse.getApparatøvelserFromResultSet(resultSet_apparatøvelse));

            Collections.sort(øvelser);
            return øvelser;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Øvelser from Treningsøkt with id=" + treningsøktID + " from the database", e);
        }
    }


    public Map<String, Integer> getStatsForApparatøvelse(Apparatøvelse apparatøvelse) {
        final String sql = "SELECT * FROM (treningsøkt_has_apparatøvelse NATURAL JOIN apparatøvelse) " +
                "WHERE idTreningsøkt = ? AND idApparatØvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID, apparatøvelse.getØvelseID());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Map<String, Integer> stats = new HashMap<>();
            stats.put("kilo", resultSet.getInt("kilo"));
            stats.put("reps", resultSet.getInt("reps"));
            stats.put("set", resultSet.getInt("set"));

            return stats;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to get stats for Øvelse from Treningsøkt from the database", e);
        }
    }
    public boolean isInInterval(LocalDate start,LocalDate end){

        if(dato.compareTo(Date.from(Instant.from(start.atStartOfDay(ZoneId.systemDefault())))) > 0){
            if(dato.compareTo(Date.from(Instant.from(end.atStartOfDay(ZoneId.systemDefault())))) < 0){
                return true;
            }
        }

        return false;
    }

    public String getStatsForFriøvelse(Friøvelse friøvelse) {
        final String sql = "SELECT treningsøkt_has_friøvelse.Beskrivelse FROM (treningsøkt_has_friøvelse " +
                "JOIN friøvelse ON treningsøkt_has_friøvelse.idFriøvelse = friøvelse.idFriøvelse) " +
                "WHERE idTreningsøkt = ? AND friøvelse.idFriøvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID, friøvelse.getØvelseID());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return resultSet.getString("beskrivelse");

        } catch (SQLException e) {
            throw new RuntimeException("Unable to get stats for Øvelse from Treningsøkt from the database", e);
        }
    }

    @Override
    public int compareTo(Treningsøkt other) {
        return this.getDato().compareTo(other.getDato());
    }
}

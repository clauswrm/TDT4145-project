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
 * Represents a Notat in the database.
 *
 * @see ActiveDomainObject
 */
@Data
public class Notat extends ActiveDomainObject implements Comparable<Notat> {

    private int notatID;
    private String tekst;
    private Treningsøkt treningsøkt;

    public Notat(int notatID, String tekst, Treningsøkt treningsøkt) {
        this.notatID = notatID;
        this.tekst = tekst;
        this.treningsøkt = treningsøkt;
    }

    public Notat(String tekst, Treningsøkt treningsøkt) {
        this.tekst = tekst;
        this.treningsøkt = treningsøkt;
    }

    @Override
    public void save() {
        if (this.tekst == null || this.treningsøkt == null) {
            throw new IllegalArgumentException("Tekst and treningsøkt must be set");
        }
        final String sql = "INSERT INTO notat (tekst, idTreningsøkt) VALUES (?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            setParameters(statement, tekst, treningsøkt.getTreningsøktID());
            statement.execute();

            // Updates the notatID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setNotatID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Notat to database", e);
        }
    }

    /**
     * Loads a Notat with the given notatID from the database.
     *
     * @param notatID the ID of the Notat to be loaded from the database.
     * @return the Notat in the database with corresponding notatID.
     * @throws RuntimeException if the given notatID was not found, or if connecting to the database failed.
     */
    public static Notat getNotatFromID(int notatID) {
        final String sql = "SELECT * FROM notat WHERE idnotat=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, notatID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String tekst = resultSet.getString("tekst");
            int treningsøktID = resultSet.getInt("idtreningsøkt");

            return new Notat(notatID, tekst, Treningsøkt.getTreningsøktFromID(treningsøktID));

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Notat with id=" + notatID + "from the database", e);
        }
    }

    /**
     * Loads all Notat from the database.
     *
     * @return a list of all Notat in the database, sorted chronologically by the corresponding Treningsøkt.
     * @throws RuntimeException if connecting to the database failed.
     */
    public static List<Notat> getAll() {
        final String sql = "SELECT * FROM notat";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Notat> results = new ArrayList<>();

            while (resultSet.next()) {
                int notatID = resultSet.getInt("idNotat");
                String tekst = resultSet.getString("tekst");
                int treningsøktID = resultSet.getInt("idTreningsøkt");

                results.add(new Notat(notatID, tekst, Treningsøkt.getTreningsøktFromID(treningsøktID)));
            }

            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Notat from the database", e);
        }
    }

    @Override
    public int compareTo(Notat other) {
        return this.getTreningsøkt().compareTo(other.getTreningsøkt());
    }
    public String toString(){
        return tekst;
    }
}

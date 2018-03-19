package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Represents a Treningsøkt in the database.
 *
 * @see ActiveDomainObject
 */
public class Treningsøkt extends ActiveDomainObject implements Comparable<Treningsøkt> {

    private int treningsøktID;
    private Date date;
    private int varighet;
    private int form;
    private int innsats;

    public Treningsøkt(int treningsøktID, Date date, int varighet, int form, int innsats) {
        this.treningsøktID = treningsøktID;
        this.date = date;
        this.varighet = varighet;
        this.form = form;
        this.innsats = innsats;
    }

    public Treningsøkt(Date date, int varighet, int form, int innsats) {
        this.date = date;
        this.varighet = varighet;
        this.form = form;
        this.innsats = innsats;
    }

    public int getTreningsøktID() {
        return treningsøktID;
    }

    public void setTreningsøktID(int treningsøktID) {
        this.treningsøktID = treningsøktID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getVarighet() {
        return varighet;
    }

    public void setVarighet(int varighet) {
        this.varighet = varighet;
    }

    public int getForm() {
        return form;
    }

    public void setForm(int form) {
        this.form = form;
    }

    public int getInnsats() {
        return innsats;
    }

    public void setInnsats(int innsats) {
        this.innsats = innsats;
    }

    @Override
    public void save() {
        if (this.date == null || this.varighet == 0) {
            throw new IllegalArgumentException("Date and varighet must be set");
        }
        final String sql = "INSERT INTO treningsøkt (dato, varighet, form, innsats) VALUES (?, ?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            setParameters(statement, date, varighet, form, innsats);
            statement.execute();

            // Updates the treningsøktID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setTreningsøktID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Treningsøkt=" + date + " to database", e);
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

            Date date = resultSet.getDate("dato");
            int varighet = resultSet.getInt("varighet");
            int form = resultSet.getInt("form");
            int innsats = resultSet.getInt("innsats");

            return new Treningsøkt(treningsøktID, date, varighet, form, innsats);

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
            List<Treningsøkt> results = new ArrayList<>();

            while (resultSet.next()) {
                int treningsøktID = resultSet.getInt("idtreningsøkt");
                Date date = resultSet.getDate("dato");
                int varighet = resultSet.getInt("varighet");
                int form = resultSet.getInt("form");
                int innsats = resultSet.getInt("innsats");

                results.add(new Treningsøkt(treningsøktID, date, varighet, form, innsats));
            }
            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Treningsøkt from the database", e);
        }
    }

    @Override
    public String toString() {
        return "Treningsøkt{" +
                "treningsøktID=" + treningsøktID +
                ", date=" + date +
                ", varighet=" + varighet +
                ", form=" + form +
                ", innsats=" + innsats +
                '}';
    }

    @Override
    public int compareTo(Treningsøkt other) {
        return this.getDate().compareTo(other.getDate());
    }
}

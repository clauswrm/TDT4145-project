package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Treningsøkt extends ActiveDomainObject {

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
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, date, varighet, form, innsats);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Treningsøkt=" + date + " to database", e);
        }
    }


    public static Treningsøkt getTreningsøktFromID(int treningsøktID) {
        final String sql = "SELECT * FROM treningsøkt WHERE idtreningsøkt=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID);
            ResultSet resultSet = statement.executeQuery();

            Date date = resultSet.getDate("date");
            int varighet = resultSet.getInt("varighet");
            int form = resultSet.getInt("form");
            int innsats = resultSet.getInt("innsats");

            return new Treningsøkt(treningsøktID, date, varighet, form, innsats);

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Treningsøkt with id=" + treningsøktID + " from the database", e);
        }
    }

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
                Date date = resultSet.getDate("date");
                int varighet = resultSet.getInt("varighet");
                int form = resultSet.getInt("form");
                int innsats = resultSet.getInt("innsats");

                results.add(new Treningsøkt(treningsøktID, date, varighet, form, innsats));
            }
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
}

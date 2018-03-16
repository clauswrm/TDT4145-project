package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public int getNotatID() {
        return notatID;
    }

    public void setNotatID(int notatID) {
        this.notatID = notatID;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public Treningsøkt getTreningsøkt() {
        return treningsøkt;
    }

    public void setTreningsøkt(Treningsøkt treningsøkt) {
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
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, notatID, tekst, treningsøkt.getTreningsøktID());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Notat to database", e);
        }
    }

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
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Notat from the database", e);
        }
    }

    @Override
    public String toString() {
        return "Notat{" +
                "notatID=" + notatID +
                ", tekst='" + tekst + '\'' +
                ", treningsøktID=" + treningsøkt.getTreningsøktID() +
                '}';
    }

    @Override
    public int compareTo(Notat other) {
        return this.getTreningsøkt().getDate().compareTo(other.getTreningsøkt().getDate());
    }
}

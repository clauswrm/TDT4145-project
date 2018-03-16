package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Notat extends ActiveDomainObject {

    private int notatID;
    private String tekst;
    private int øktID;

    public Notat(String tekst, int øktID) {
        this.tekst = tekst;
        this.øktID = øktID;
    }

    public Notat(int notatID, String tekst, int øktID) {
        this.notatID = notatID;
        this.tekst = tekst;
        this.øktID = øktID;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    @Override
    public void save() {
        if (this.tekst == null) {
            throw new IllegalArgumentException("Navn og beskrivelse må være satt");
        }
        final String sql = "INSERT INTO notat (idNotat, tekst, idTreningsøkt)" +
                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE tekst=?, idTreningsøkt=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, notatID, tekst, øktID, tekst, øktID);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Feil: Notat ble ikke lagret til databasen.", e);
        }
    }

    @Override
    public void load() {
        final String sql = "SELECT * FROM notat WHERE idnotat=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, notatID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.notatID = resultSet.getInt("idnotat");
                this.tekst = resultSet.getString("tekst");
                this.øktID = resultSet.getInt("idtreningsøkt");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Feil: Klarte ikke hente notat!");
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
                int notatID = resultSet.getInt("idnotat");
                String tekst = resultSet.getString("tekst");
                int øktID = resultSet.getInt("idtreningsøkt");

                results.add(new Notat(notatID, tekst, øktID));
            }

            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Feil: Klarte ikke hente notater");
        }
    }

    @Override
    public String toString() {
        return "Notat{" +
                "notatID=" + notatID +
                ", tekst='" + tekst + '\'' +
                ", øktID=" + øktID +
                '}';
    }
}

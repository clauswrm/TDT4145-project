package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public int getØvelsesgruppeID() {
        return øvelsesgruppeID;
    }

    public void setØvelsesgruppeID(int øvelsesgruppeID) {
        this.øvelsesgruppeID = øvelsesgruppeID;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
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
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, navn);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Øvelsesgruppe=" + navn + " to database", e);
        }
    }

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

            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Øvelsesgruppe from the database", e);
        }
    }

    @Override
    public String toString() {
        return "Øvelsesgruppe{" +
                "øvelsesgruppeID=" + øvelsesgruppeID +
                ", navn='" + navn + '\'' +
                '}';
    }

    @Override
    public int compareTo(Øvelsesgruppe other) {
        return this.getNavn().compareTo(other.getNavn());
    }
}

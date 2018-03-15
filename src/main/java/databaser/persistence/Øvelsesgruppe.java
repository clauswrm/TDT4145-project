package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Øvelsesgruppe extends ActiveDomainObject {

    private int øvelsesgruppeID;
    private String navn;

    public Øvelsesgruppe(int øvelsesgruppeID, String navn) {
        this.øvelsesgruppeID = øvelsesgruppeID;
        this.navn = navn;
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
            throw new IllegalArgumentException("Navn må være satt");
        }

        final String sql = "INSERT INTO øvelsegruppe (idØvelsegruppe, Navn)" +
                "VALUES (?, ?) ON DUPLICATE KEY UPDATE Navn=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, øvelsesgruppeID, navn);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save to database.", e);
        }
    }

    @Override
    public void load() {
        final String sql = "SELECT * FROM øvelsegruppe WHERE idØvelsegruppe=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, øvelsesgruppeID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.øvelsesgruppeID = resultSet.getInt("idØvelsesgruppe");
                this.navn = resultSet.getString("Navn");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save apparatus to database");
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
            throw new RuntimeException("Unable to load stuff from the database");
        }
    }
}

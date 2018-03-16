package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, navn, beskrivelse);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Friøvelse=" + navn + " to the database", e);
        }
    }

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

    public static List<Friøvelse> getAllFriøvelser() {
        final String sql = "SELECT * FROM friøvelse";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Friøvelse> results = new ArrayList<>();

            while (resultSet.next()) {
                int øvelseID = resultSet.getInt("idFriøvelse");
                String navn = resultSet.getString("Navn");
                String beskrivelse = resultSet.getString("Beskrivelse");

                results.add(new Friøvelse(øvelseID, navn, beskrivelse));
            }
            Collections.sort(results);
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Friøvelse from the database", e);
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

package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Apparat extends ActiveDomainObject {

    private int apparatID;
    private String navn;
    private String beskrivelse;

    public Apparat(int apparatID, String navn, String beskrivelse) {
        this.apparatID = apparatID;
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public Apparat(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public int getApparatID() {
        return apparatID;
    }

    public void setApparatID(int apparatID) {
        this.apparatID = apparatID;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    @Override
    public void save() {
        if (this.navn == null || this.beskrivelse == null) {
            throw new IllegalArgumentException("Navn and beskrivelse must be set");
        }

        final String sql = "INSERT INTO apparat (Navn, Beskrivelse) VALUES (?, ?)";
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, navn, beskrivelse);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Apparat=" + navn + " to database", e);
        }
    }

    public static Apparat getApparatFromID(int apparatID) {
        final String sql = "SELECT * FROM apparat WHERE apparatID = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, apparatID);
            ResultSet resultSet = statement.executeQuery();

            String navn = resultSet.getString("Navn");
            String beskrivelse = resultSet.getString("Beskrivelse");

            return new Apparat(apparatID, navn, beskrivelse);

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Apparat with id=" + apparatID + " from the database", e);
        }
    }

    public static List<Apparat> getAll() {
        final String sql = "SELECT * FROM apparat";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Apparat> results = new ArrayList<>();

            while (resultSet.next()) {
                int apparatID = resultSet.getInt("apparatID");
                String navn = resultSet.getString("Navn");
                String beskrivelse = resultSet.getString("Beskrivelse");

                results.add(new Apparat(apparatID, navn, beskrivelse));
            }
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Apparat from the database", e);
        }
    }

    @Override
    public String toString() {
        return "Apparat{" +
                "apparatID=" + apparatID +
                ", navn='" + navn + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                '}';
    }
}

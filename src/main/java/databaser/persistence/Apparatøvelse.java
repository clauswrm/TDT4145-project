package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Apparatøvelse extends Øvelse {

    private Apparat apparat;

    public Apparatøvelse(int øvelseID, String navn, String beskrivelse, Apparat apparat) {
        super(øvelseID, navn, beskrivelse);
        this.apparat = apparat;
    }

    public Apparatøvelse(String navn, String beskrivelse, Apparat apparat) {
        super(navn, beskrivelse);
        this.apparat = apparat;
    }

    public Apparat getApparat() {
        return apparat;
    }

    public void setApparat(Apparat apparat) {
        this.apparat = apparat;
    }

    @Override
    public void save() {
        if (this.navn == null || this.beskrivelse == null) {
            throw new IllegalArgumentException("Navn, apparat and beskrivelse must be set");
        }

        final String sql = "INSERT INTO apparatøvelse (Navn, apparatID, Beskrivelse)" +
                "VALUES (?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, navn, apparat.getApparatID(), beskrivelse);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Apparatøvelse=" + navn + " to the database", e);
        }
    }

    public static Apparatøvelse getApparatøvelseFromID(int øvelseID) {
        final String sql = "SELECT * FROM apparatøvelse WHERE idApparatØvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, øvelseID);
            ResultSet resultSet = statement.executeQuery();

            String navn = resultSet.getString("Navn");
            int apparatID = resultSet.getInt("apparatID");
            String beskrivelse = resultSet.getString("Beskrivelse");

            return new Apparatøvelse(øvelseID, navn, beskrivelse, Apparat.getApparatFromID(apparatID));

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Apparatøvelse with id=" + øvelseID + " from the database", e);
        }
    }

    public static List<Apparatøvelse> getAll() {
        final String sql = "SELECT * FROM apparatøvelse";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Apparatøvelse> results = new ArrayList<>();

            while (resultSet.next()) {
                int øvelseID = resultSet.getInt("idApparatØvelse");
                String navn = resultSet.getString("Navn");
                int apparatID = resultSet.getInt("apparatID");
                String beskrivelse = resultSet.getString("Beskrivelse");

                results.add(new Apparatøvelse(øvelseID, navn, beskrivelse, Apparat.getApparatFromID(apparatID)));
            }
            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load all Apparatøvelse from the database", e);
        }
    }

    @Override
    public String toString() {
        return "Apparatøvelse{" +
                "øvelseID=" + øvelseID +
                ", apparatID=" + apparat.getApparatID() +
                ", navn='" + navn + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                '}';
    }
}

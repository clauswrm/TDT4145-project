package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an Apparatøvelse in the database.
 *
 * @see ActiveDomainObject
 */
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
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            setParameters(statement, navn, apparat.getApparatID(), beskrivelse);
            statement.execute();

            // Updates the øvelseID with the auto generated key
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                setØvelseID(generatedKeys.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save Apparatøvelse=" + navn + " to the database", e);
        }
    }

    /**
     * Loads an Apparatøvelse with the given øvelseID from the database.
     *
     * @param øvelseID the ID of the Apparatøvelse to be loaded from the database.
     * @return the Apparatøvelse in the database with corresponding øvelseID.
     * @throws RuntimeException if the given øvelseID was not found, or if connecting to the database failed.
     */
    public static Apparatøvelse getApparatøvelseFromID(int øvelseID) {
        final String sql = "SELECT * FROM apparatøvelse WHERE idApparatØvelse = ?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            setParameters(statement, øvelseID);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String navn = resultSet.getString("Navn");
            int apparatID = resultSet.getInt("apparatID");
            String beskrivelse = resultSet.getString("Beskrivelse");

            return new Apparatøvelse(øvelseID, navn, beskrivelse, Apparat.getApparatFromID(apparatID));

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load Apparatøvelse with id=" + øvelseID + " from the database", e);
        }
    }

    /**
     * Loads all Apparatøvelse from the database.
     *
     * @return a list of all Apparatøvelse in the database, sorted alphabetically by name.
     * @throws RuntimeException if connecting to the database failed.
     */
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
            Collections.sort(results);
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

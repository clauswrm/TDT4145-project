package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Apparatøvelse extends ActiveDomainObject {

    private int apparatØvelseID;
    private String navn;
    private int apparatID;
    private String beskrivelse;

    public Apparatøvelse(int apparatØvelseID, String navn, int apparatID, String beskrivelse) {
        this.apparatØvelseID = apparatØvelseID;
        this.navn = navn;
        this.apparatID = apparatID;
        this.beskrivelse = beskrivelse;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public int getApparatID() {
        return apparatID;
    }

    public void setApparatID(int apparatID) {
        this.apparatID = apparatID;
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
            throw new IllegalArgumentException("Navn, apparat og beskrivelse må være satt");
        }

        final String sql = "INSERT INTO apparatøvelse (idApparatØvelse, Navn, idApparat, Beskrivelse)" +
                "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE Navn=?, idApparat=?, Beskrivelse=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, apparatØvelseID, navn, apparatID, beskrivelse);
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

            setParameters(statement, apparatØvelseID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.apparatØvelseID = resultSet.getInt("apparatØvelseID");
                this.navn = resultSet.getString("Navn");
                this.apparatID = resultSet.getInt("idApparat");
                this.beskrivelse = resultSet.getString("Beskrivelse");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save apparatus to database");
        }
    }
}

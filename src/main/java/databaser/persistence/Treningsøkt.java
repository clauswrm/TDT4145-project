package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Treningsøkt {


/*

        public Treningsøkt(int apparatID, String navn, String beskrivelse) {
            this.apparatID = apparatID;
            this.navn = navn;
            this.beskrivelse = beskrivelse;
        }


        @Override
        public void save() {
            if (this.navn == null || this.beskrivelse == null) {
                throw new IllegalArgumentException("Navn og beskrivelse må være satt");
            }
            final String sql = "INSERT INTO apparat (idApparat, Navn, Beskrivelse)" +
                    "VALUES (?, ?, ?)";

            try (
                    Connection connection = getConnection();
                    PreparedStatement statement = connection.prepareStatement(sql);
            ) {

                setParameters(statement, apparatID, navn, beskrivelse);
                statement.execute();

            } catch (SQLException e) {
                throw new RuntimeException("Unable to save apparatus to database.", e);
            }
        }



        @Override
        public void load() {
            final String sql = "SELECT * FROM apparat WHERE idApparat=?";

            try (
                    Connection connection = getConnection();
                    PreparedStatement statement = connection.prepareStatement(sql);
            ) {

                setParameters(statement, apparatID);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    this.apparatID = resultSet.getInt("idApparat");
                    this.navn = resultSet.getString("Navn");
                    this.beskrivelse = resultSet.getString("Beskrivelse");
                }

            } catch (SQLException e) {
                throw new RuntimeException("Unable to save apparatus to database");
            }
        }

        public static List<databaser.persistence.Apparat> getAll() {
            final String sql = "SELECT * FROM apparat";

            try (
                    Connection connection = getConnection();
                    PreparedStatement statement = connection.prepareStatement(sql);
            ) {

                ResultSet resultSet = statement.executeQuery();
                List<databaser.persistence.Apparat> results = new ArrayList<>();

                while (resultSet.next()) {
                    int apparatID = resultSet.getInt("idApparat");
                    String navn = resultSet.getString("Navn");
                    String beskrivelse = resultSet.getString("Beskrivelse");

                    results.add(new databaser.persistence.Apparat(apparatID, navn, beskrivelse));
                }

                return results;

            } catch (SQLException e) {
                throw new RuntimeException("Unable to load apparatuses from the database");
            }
        } */
    }

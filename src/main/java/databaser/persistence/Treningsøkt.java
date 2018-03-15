package databaser.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Treningsøkt extends ActiveDomainObject {

    private int treningsøktID;
    private Date date;
    private Time tidspunkt;
    int varighet;
    int form;
    int innsats;


    public Treningsøkt(int treningsøktID, Date date, int varighet, int form, int innsats) {
        this.treningsøktID = treningsøktID;

        this.date = date;
        this.varighet = varighet;
        this.form = form;
        this.innsats = innsats;
    }


    @Override
    public void save() {
        final String sql = "INSERT INTO treningsøkt (idtreningsøkt, dato, varighet, form, innsats)" +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID, date, varighet, form, innsats);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Feil: klarte ikke sette inn treningsøkt", e);
        }
    }


    @Override
    public void load() {
        final String sql = "SELECT * FROM treningsøkt WHERE idtreningsøkt=?";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, treningsøktID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                this.treningsøktID = resultSet.getInt("idtreningsøkt");
                this.date = resultSet.getDate("date");

                this.varighet = resultSet.getInt("varighet");
                this.form = resultSet.getInt("form");
                this.innsats = resultSet.getInt("innsats");

            }

        } catch (SQLException e) {
            throw new RuntimeException("Feil: klarte ikke lagre treningsøkt");
        }
    }

    public static List<Treningsøkt> getAll() {
        final String sql = "SELECT * FROM treningsøkt";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            ResultSet resultSet = statement.executeQuery();
            List<Treningsøkt> results = new ArrayList<>();

            while (resultSet.next()) {
                int treningsøktID = resultSet.getInt("idtreningsøkt");
                Date date = resultSet.getDate("date");

                int varighet = resultSet.getInt("varighet");
                int form = resultSet.getInt("form");
                int innsats = resultSet.getInt("innsats");

                results.add(new Treningsøkt(treningsøktID, date, varighet, form, innsats));
            }

            return results;

        } catch (SQLException e) {
            throw new RuntimeException("Feil: klarte ikke hente treningsøkter fra databasen");
        }
    }
}

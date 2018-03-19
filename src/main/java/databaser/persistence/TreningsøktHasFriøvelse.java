package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TreningsøktHasFriøvelse extends ActiveDomainObject {

    protected int treningsøktID;

    public int getTreningsøktID() {
        return treningsøktID;
    }

    public void setTreningsøktID(int treningsøktID) {
        this.treningsøktID = treningsøktID;
    }

    public int getFriøvelseID() {
        return friøvelseID;
    }

    public void setFriøvelseID(int friøvelseID) {
        this.friøvelseID = friøvelseID;
    }

    protected int friøvelseID;


    public TreningsøktHasFriøvelse(int friøvelseID, int treningsøktID){
        this.friøvelseID = friøvelseID;
        this.treningsøktID = treningsøktID;
    }

    @Override
    public void save() {

        final String sql = "INSERT INTO treningsøkt_has_friøvelse (idTreningsøkt, idfriøvelse) VALUES (?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            setParameters(statement, friøvelseID, treningsøktID);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save table references to database", e);
        }
    }
}

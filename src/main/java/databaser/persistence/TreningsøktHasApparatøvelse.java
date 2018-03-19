package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TreningsøktHasApparatøvelse extends ActiveDomainObject {

    protected int treningsøktID;

    public int getTreningsøktID() {
        return treningsøktID;
    }

    public void setTreningsøktID(int treningsøktID) {
        this.treningsøktID = treningsøktID;
    }

    public int getApparatøvelseID() {
        return apparatøvelseID;
    }

    public void setApparatøvelseID(int apparatøvelseID) {
        this.apparatøvelseID = apparatøvelseID;
    }

    protected int apparatøvelseID;


    public TreningsøktHasApparatøvelse(int apparatøvelseID, int treningsøktID){
        this.apparatøvelseID = apparatøvelseID;
        this.treningsøktID = treningsøktID;
    }

    @Override
    public void save() {

        final String sql = "INSERT INTO treningsøkt_has_apparatøvelse (idTreningsøkt, idapparatøvelse) VALUES (?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
        ) {

            setParameters(statement, apparatøvelseID, treningsøktID);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save table references to database", e);
        }
    }
}

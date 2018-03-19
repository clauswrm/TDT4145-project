package databaser.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FriøvelseHasØvelsesgruppe extends ActiveDomainObject{


    protected int friøvelseID;

    public int getFriøvelseID() {
        return friøvelseID;
    }

    public void setFriøvelseID(int friøvelseID) {
        this.friøvelseID = friøvelseID;
    }

    public int getØvelsesgruppeID() {
        return øvelsesgruppeID;
    }

    public void setØvelsesgruppeID(int øvelsesgruppeID) {
        this.øvelsesgruppeID = øvelsesgruppeID;
    }

    protected int øvelsesgruppeID;


    public FriøvelseHasØvelsesgruppe(int friøvelseID, int øvelsesgruppeID){
        this.friøvelseID = friøvelseID;
        this.øvelsesgruppeID = øvelsesgruppeID;
    }

    @Override
    public void save() {
        final String sql = "INSERT INTO apparatøvelse_has_øvelsesgruppe (idfriøvelse, idøvelsesgruppe) VALUES (?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            setParameters(statement, friøvelseID, øvelsesgruppeID);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save table references to database", e);
        }
    }
}

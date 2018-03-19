package databaser.persistence;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ApparatøvelseHasØvelsesgruppe extends ActiveDomainObject {

    public int getApparatøvelseID() {
        return apparatøvelseID;
    }

    public void setApparatøvelseID(int apparatøvelseID) {
        this.apparatøvelseID = apparatøvelseID;
    }

    protected int apparatøvelseID;

    public int getØvelsesgruppeID() {
        return øvelsesgruppeID;
    }

    public void setØvelsesgruppeID(int øvelsesgruppeID) {
        this.øvelsesgruppeID = øvelsesgruppeID;
    }

    protected int øvelsesgruppeID;


    public ApparatøvelseHasØvelsesgruppe(int apparatøvelseID, int øvelsesgruppeID){
        this.apparatøvelseID = apparatøvelseID;
        this.øvelsesgruppeID = øvelsesgruppeID;
    }

    @Override
    public void save() {
        final String sql = "INSERT INTO apparatøvelse_has_øvelsesgruppe (idapparatøvelse, idøvelsesgruppe) VALUES (?, ?)";

        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            setParameters(statement, apparatøvelseID, øvelsesgruppeID);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to save table references to database", e);
        }
    }
    }


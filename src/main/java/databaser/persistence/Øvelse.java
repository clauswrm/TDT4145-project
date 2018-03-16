package databaser.persistence;

public abstract class Øvelse extends ActiveDomainObject {

    protected int øvelseID;
    protected String navn;
    protected String beskrivelse;

    public Øvelse(int øvelseID, String navn, String beskrivelse) {
        this.øvelseID = øvelseID;
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public Øvelse(String navn, String beskrivelse) {
        this.navn = navn;
        this.beskrivelse = beskrivelse;
    }

    public int getØvelseID() {
        return øvelseID;
    }

    public String getNavn() {
        return navn;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setØvelseID(int øvelseID) {
        this.øvelseID = øvelseID;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }
}

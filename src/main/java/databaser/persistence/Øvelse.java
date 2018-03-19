package databaser.persistence;

import java.util.List;

/**
 * Represents a Øvelse in the database. Is a superclass for {@link Friøvelse} and {@link Apparatøvelse}.
 *
 * @see ActiveDomainObject
 */
public abstract class Øvelse extends ActiveDomainObject implements Comparable<Øvelse> {

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

    public abstract void addToØvelsesgruppe(Øvelsesgruppe øvelsesgruppe);

    public abstract List<Treningsøkt> getTreningsøkterWithØvelse();

    @Override
    public int compareTo(Øvelse other) {
        return this.getNavn().compareTo(other.getNavn());
    }
}

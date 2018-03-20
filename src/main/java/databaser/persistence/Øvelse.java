package databaser.persistence;

import lombok.Data;

import java.util.List;

/**
 * Represents a Øvelse in the database. Is a superclass for {@link Friøvelse} and {@link Apparatøvelse}.
 *
 * @see ActiveDomainObject
 */
@Data
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

    public abstract void addToØvelsesgruppe(Øvelsesgruppe øvelsesgruppe);

    public abstract List<Treningsøkt> getTreningsøkterWithØvelse();

    @Override
    public int compareTo(Øvelse other) {
        return this.getNavn().compareTo(other.getNavn());
    }
}

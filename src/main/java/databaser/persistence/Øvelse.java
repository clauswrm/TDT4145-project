package databaser.persistence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Override
    public int compareTo(Øvelse other) {
        return this.getNavn().compareTo(other.getNavn());
    }

    public static List<Øvelse> getAllØvelser() {
        List<Øvelse> øvelser = new ArrayList<>();
        øvelser.addAll(Friøvelse.getAllFriøvelser());
        øvelser.addAll(Apparatøvelse.getAllAparatøvelser());

        Collections.sort(øvelser);
        return øvelser;
    }
}

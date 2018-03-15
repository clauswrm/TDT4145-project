package databaser;

import databaser.persistence.Apparat;

public class Main {
    public static void main(String[] args) {
        Apparat benk = new Apparat(1, "Benk", "Det er en benk. Ta benkpress.");
        benk.save();
    }
}

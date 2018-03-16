package databaser;

import databaser.persistence.*;

import java.util.Date;

public class Main {
    public static void main(String[] args) {

        Notat notat = Notat.getNotatFromID(1);
        System.out.println(notat.getTekst());
    }
}

package databaser;

import databaser.persistence.*;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        //Treningsøkt treningsøkt = new Treningsøkt(1, new Date(), 66, 5,7);
        //treningsøkt.save();
        Notat notat = new Notat(1, "GO KYS", 1);
        notat.save();
    }
}

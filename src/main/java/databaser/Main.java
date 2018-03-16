package databaser;

import databaser.persistence.*;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        //Treningsøkt treningsøkt = new Treningsøkt(1, new Date(), 66, 5,7);
        //treningsøkt.save();
        Apparat one = new Apparat(1,"Kettlebells","U do stuff wiffem");
        Apparat two = new Apparat(2,"Manualer","Dey me non automatic");

        one.save();
        two.save();
    }
}

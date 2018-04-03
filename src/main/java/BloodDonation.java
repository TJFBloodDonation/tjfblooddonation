import ro.ubb.model.Blood;

import java.sql.Date;

public class BloodDonation {
    public static void main(String[] args) {
        Blood m = new Blood("sfds","donno",19L, Date.valueOf("2001-01-01"),12L,false,"Sdas");
        System.out.println(m);
    }
}

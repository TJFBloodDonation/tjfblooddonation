package ro.ubb.tjfblooddonation.utils;

import ro.ubb.tjfblooddonation.model.Donor;

import java.time.LocalDateTime;

public class InfoCheck {

    public static String canDonate(Donor donor) {
        String err = "";

        if(donor.getForm() == null )
            return "No form found for donor";

        if (!donor.getForm().getPassedBasicCheckForm())
            err += "Donor can't donate because they did not pass the basic check form.\n";

        if(!donor.getForm().getPassedDonateForm())
            err += "Donor can't donate because they did not pass the check form they completed prior to donation. \n";

        if (!donor.getForm().getTimeCompletedDonateForm().toLocalDateTime().plusDays(7).isAfter(LocalDateTime.now()))
            err += "Donor can't donate because the check form was completed more than a week prior to donation. \n";

        return err;
    }
}

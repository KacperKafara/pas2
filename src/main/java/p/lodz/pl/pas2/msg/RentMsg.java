package p.lodz.pl.pas2.msg;

public class RentMsg {
    public static final String  RENTS_NOT_FOUND = "There is not rents";
    public static final String WRONG_END_DATE = "EndDate is before StartDate.";
    public static final String RENT_NOT_FOUND = "Rent with given id does not exist.";
    public static final String RENT_NOT_ENDED = "The rental is still ongoing.";
    public static final String WRONG_START_DATE = "Start date cannot be set in the past.";
    public static final String RENT_FOR_WRONG_USER = "You tried to create a rent for a user other than the client.";
    public static final String RENT_IS_ALREADY_ENDED = "The rental is already finished.";
    public static final String RENT_FOR_OTHER_CLIENT = "You tried to start a rental for another client.";
}

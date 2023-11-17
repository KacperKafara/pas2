package p.lodz.pl.pas2.msg;

public class RentMsg {
    public static String WRONG_END_DATE = "EndDate is before StartDate.";
    public static String RENT_NOT_FOUND = "Rent with given id does not exist.";
    public static String RENT_NOT_ENDED = "The rental is still ongoing.";
    public static String WRONG_START_DATE = "Start date cannot be set in the past.";
}

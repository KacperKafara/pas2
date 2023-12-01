package p.lodz.pl.pas2.exceptions.rentExceptions;

public class RentNotForClientException extends RuntimeException {
    public RentNotForClientException(String msg) {
        super(msg);
    }
}

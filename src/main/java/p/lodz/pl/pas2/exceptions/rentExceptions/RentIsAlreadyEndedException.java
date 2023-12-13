package p.lodz.pl.pas2.exceptions.rentExceptions;

public class RentIsAlreadyEndedException extends RuntimeException {
    public RentIsAlreadyEndedException(String msg) {
        super(msg);
    }
}

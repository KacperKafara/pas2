package p.lodz.pl.pas2.exceptions.rentExceptions;

public class ThereIsNoSuchRentToUpdateException extends RuntimeException {
    public ThereIsNoSuchRentToUpdateException(String msg) {
        super(msg);
    }
}

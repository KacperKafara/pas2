package p.lodz.pl.pas2.exceptions.rentExceptions;

public class ThereIsNoSuchRentToDelete extends RuntimeException {
    public ThereIsNoSuchRentToDelete(String msg) {
        super(msg);
    }
}

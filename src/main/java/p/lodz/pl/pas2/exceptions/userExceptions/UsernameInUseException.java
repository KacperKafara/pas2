package p.lodz.pl.pas2.exceptions.userExceptions;

public class UsernameInUseException extends RuntimeException {
    public UsernameInUseException(String msg) {
        super(msg);
    }
}

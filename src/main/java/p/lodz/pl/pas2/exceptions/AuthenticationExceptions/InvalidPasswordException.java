package p.lodz.pl.pas2.exceptions.AuthenticationExceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String wrongPassword) {
        super(wrongPassword);
    }
}

package p.lodz.pl.pas2.exceptions.userExceptions;

public class UsersNotFoundException extends RuntimeException {
    public UsersNotFoundException(String msg) {
        super(msg);
    }
}

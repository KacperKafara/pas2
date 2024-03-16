package p.lodz.pl.pas2.exceptions.AuthenticationExceptions;

public class JwsNotMatchException extends RuntimeException {
    public JwsNotMatchException(String msg) {
        super(msg);
    }
}

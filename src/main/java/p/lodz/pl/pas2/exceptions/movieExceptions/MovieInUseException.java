package p.lodz.pl.pas2.exceptions.movieExceptions;

public class MovieInUseException extends RuntimeException {
    public MovieInUseException(String msg) {
        super(msg);
    }
}

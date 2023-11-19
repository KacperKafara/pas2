package p.lodz.pl.pas2.exceptions.movieExceptions;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String msg) {
        super(msg);
    }
}

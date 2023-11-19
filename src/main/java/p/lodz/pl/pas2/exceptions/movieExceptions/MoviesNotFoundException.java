package p.lodz.pl.pas2.exceptions.movieExceptions;

public class MoviesNotFoundException extends RuntimeException {
    public MoviesNotFoundException(String msg) {
        super(msg);
    }
}

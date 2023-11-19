package p.lodz.pl.pas2.exceptions.movieExceptions;

public class ThereIsNoSuchMovieToUpdateException extends RuntimeException {
    public ThereIsNoSuchMovieToUpdateException(String msg) {
        super(msg);
    }
}

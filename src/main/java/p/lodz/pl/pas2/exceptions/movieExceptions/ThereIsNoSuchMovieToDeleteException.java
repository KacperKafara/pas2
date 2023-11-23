package p.lodz.pl.pas2.exceptions.movieExceptions;

public class ThereIsNoSuchMovieToDeleteException extends RuntimeException{
    public ThereIsNoSuchMovieToDeleteException(String message) {
        super(message);
    }
}

package p.lodz.pl.pas2.repositories;

import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRepository  {

    Movie findMovie(UUID id);
    List<Movie> findMovies();
    Movie saveMovie(Movie movie);
    Movie updateMovie(UUID id, Movie movie);
    boolean deleteMovie(UUID id);
}

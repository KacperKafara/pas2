package p.lodz.pl.pas2.repositories;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Movie;

import java.util.List;
import java.util.UUID;

public interface MovieRepository {
    Movie findMovie(UUID id);
    List<Movie> findMovies();
    Movie saveMovie(Movie movie);
    Movie changeTitle(UUID id, String title);
    Movie changeCost(UUID id, double cost);
    boolean deleteMovie(UUID id);
}

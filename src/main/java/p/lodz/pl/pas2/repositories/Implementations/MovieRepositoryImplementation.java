package p.lodz.pl.pas2.repositories.Implementations;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MovieRepositoryImplementation implements MovieRepository {
    private final List<Movie> movies;
    public MovieRepositoryImplementation() {
        movies = new ArrayList<>();
    }
    @Override
    public Movie findMovie(UUID id) {
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                return movie;
            }
        }
        return null;
    }

    @Override
    public List<Movie> findMovies() {
        return movies;
    }

    @Override
    public Movie saveMovie(Movie movie) {
        movie.setId(UUID.randomUUID());
        movies.add(movie);
        return movie;
    }

    @Override
    public Movie updateMovie(UUID id, Movie movie) {
        Movie updatedMovie = findMovie(id);
        updatedMovie.setTitle(movie.getTitle());
        updatedMovie.setCost(movie.getCost());
        return updatedMovie;
    }

    @Override
    public boolean deleteMovie(UUID id) {
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                movies.remove(movie);
                return true;
            }
        }
        return false;
    }
}

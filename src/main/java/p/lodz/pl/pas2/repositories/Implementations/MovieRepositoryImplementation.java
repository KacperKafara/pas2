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
        movies.add(movie);
        return movie;
    }

    @Override
    public Movie changeTitle(UUID id, String title) {
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                movie.setTitle(title);
                return movie;
            }
        }
        return null;
    }

    @Override
    public Movie changeCost(UUID id, double cost) {
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                movie.setCost(cost);
                return movie;
            }
        }
        return null;
    }

    @Override
    public boolean deleteMovie(UUID id) {
        Movie movieToRemove = null;
        for (Movie movie : movies) {
            if (movie.getId().equals(id)) {
                movieToRemove = movie;
                break;
            }
        }

        if (movieToRemove != null) {
            movies.remove(movieToRemove);
            return true;
        } else {
            return false;
        }
    }
}

package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.repositories.MovieRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository repository;

    @Autowired
    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public Movie getMovie(UUID id) {
        return repository.findMovie(id);
    }

    public List<Movie> getMovies() {
        return repository.findMovies();
    }

    public Movie addMovie(Movie movie) {
        if(movie.getCost() <= 0) return null;
        if(movie.getTitle().isEmpty()) return null;
        return repository.saveMovie(movie);
    }

    public Movie updateMovie(UUID id, Movie movie) {
        if(movie.getTitle().isEmpty() || movie.getCost() <= 0) return null;
        return repository.updateMovie(id, movie);
    }

    public boolean deleteMovie(UUID id) {
        return repository.deleteMovie(id);
    }
}

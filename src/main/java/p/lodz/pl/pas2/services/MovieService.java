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

    public Movie saveMovie(Movie movie) {
        return repository.saveMovie(movie);
    }

    public Movie changeTitle(UUID id, String title) {
        return repository.changeTitle(id, title);
    }

    public Movie changeCost(UUID id, double cost) {
        return repository.changeCost(id, cost);
    }

    public boolean deleteMovie(UUID id) {
        return repository.deleteMovie(id);
    }
}

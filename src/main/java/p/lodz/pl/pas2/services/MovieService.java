package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.repositories.Implementations.MovieRepositoryImplementation;
import p.lodz.pl.pas2.repositories.Implementations.RentRepositoryImplementation;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.MovieRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.RentRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.MovieRepository;
import p.lodz.pl.pas2.repositories.RentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RentRepository rentRepository;

    @Autowired
    public MovieService(@Qualifier("movieRepositoryMongoDB") MovieRepository movieRepository,@Qualifier("rentRepositoryMongoDB") RentRepository rentRepository) {
        this.movieRepository = movieRepository;
        this.rentRepository = rentRepository;
    }

    public Movie getMovie(UUID id) {
        return movieRepository.findMovie(id);
    }

    public List<Movie> getMovies() {
        return movieRepository.findMovies();
    }

    public Movie addMovie(Movie movie) {
        if(movie.getCost() <= 0) return null;
        if(movie.getTitle().isEmpty()) return null;
        return movieRepository.saveMovie(movie);
    }

    public Movie updateMovie(UUID id, Movie movie) {
        if(movie.getTitle().isEmpty() || movie.getCost() <= 0) return null;
        return movieRepository.updateMovie(id, movie);
    }

    public boolean deleteMovie(UUID id) {
        List<Rent> rents = rentRepository.findCurrentRents();
        for(Rent rent : rents) {
            if(rent.getMovie().getId() == id) return false;
        }
        return movieRepository.deleteMovie(id);
    }
}

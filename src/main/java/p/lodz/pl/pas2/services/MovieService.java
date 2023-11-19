package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieInUseException;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieNotFoundException;
import p.lodz.pl.pas2.exceptions.movieExceptions.MoviesNotFoundException;
import p.lodz.pl.pas2.exceptions.movieExceptions.ThereIsNoSuchMovieToUpdateException;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.msg.MovieMsg;
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
        Movie movie = movieRepository.findMovie(id);
        if(movie == null) throw new MovieNotFoundException(MovieMsg.MOVIE_NOT_FOUND);
        return movie;
    }

    public List<Movie> getMovies() {
        List<Movie> movies = movieRepository.findMovies();
        if(movies.isEmpty()) throw new MoviesNotFoundException(MovieMsg.MOVIES_NOT_FOUND);
        return movies;
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.saveMovie(movie);
    }

    public Movie updateMovie(UUID id, Movie updatedMovie) {
        Movie movie = movieRepository.findMovie(id);
        if(movie == null) throw new ThereIsNoSuchMovieToUpdateException(MovieMsg.MOVIE_NOT_FOUND);
        return movieRepository.updateMovie(id, updatedMovie);
    }

    public boolean deleteMovie(UUID id) {
        List<Rent> rents = rentRepository.findCurrentRents();
        Movie movie = movieRepository.findMovie(id);
        if (movie == null) throw new MovieNotFoundException(MovieMsg.MOVIE_NOT_FOUND);
        for(Rent rent : rents) {
            if(Objects.equals(rent.getMovie().getId().toString(), id.toString())) throw new MovieInUseException(MovieMsg.MOVIE_IS_RENTED);
        }
        return movieRepository.deleteMovie(id);
    }
}

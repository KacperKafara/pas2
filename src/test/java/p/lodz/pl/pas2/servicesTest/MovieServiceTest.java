package p.lodz.pl.pas2.servicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import p.lodz.pl.pas2.exceptions.MovieException;
import p.lodz.pl.pas2.exceptions.UserNotFound;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.model.UserType;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.MovieRepository;
import p.lodz.pl.pas2.repositories.RentRepository;
import p.lodz.pl.pas2.services.MovieService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private RentRepository rentRepository;
    @InjectMocks
    MovieService movieService;
    Movie movie;
    @BeforeEach
    public void setup(){
        movie = new Movie("test",50);
    }

    @Test
    public void getMovieByIdAndMovieExist() {
        UUID movieId = UUID.randomUUID();
        given(movieRepository.findMovie(movieId)).willReturn(movie);

        Movie checked = movieService.getMovie(movieId);

        assertNotNull(checked);
        assertEquals(movie.getTitle(), checked.getTitle());
        assertEquals(movie.getCost(), checked.getCost());
    }

    @Test
    public void getMovies() {
        Movie movie2 = new Movie("test2", 30);
        given(movieRepository.findMovies()).willReturn(List.of(movie, movie2));

        List<Movie> movies = movieService.getMovies();

        assertEquals(2, movies.size());
        assertEquals(movie.getTitle(), movies.get(0).getTitle());
        assertEquals(movie.getCost(), movies.get(0).getCost());
        assertEquals(movie2.getTitle(), movies.get(1).getTitle());
        assertEquals(movie2.getCost(), movies.get(1).getCost());
    }

    @Test
    public void addMovie() {
        // Arrange
        given(movieRepository.saveMovie(any(Movie.class))).willReturn(movie);

        // Act
        Movie addedMovie = movieService.addMovie(movie);

        // Assert
        assertNotNull(addedMovie);
        assertEquals(movie.getTitle(), addedMovie.getTitle());
        assertEquals(movie.getCost(), addedMovie.getCost());
    }

   /* @Test
    public void updateMovieAndMovieExist() {
        given(movieRepository.findMovie(movie.getId())).willReturn(movie);
        Movie updatedMovie = new Movie("updated", 20);
        given(movieRepository.updateMovie(movie.getId(), updatedMovie)).willReturn(updatedMovie);

        Movie checked = movieService.updateMovie(movie.getId(), updatedMovie);

        assertNotNull(checked);
        assertEquals(updatedMovie.getTitle(), checked.getTitle());
        assertEquals(updatedMovie.getCost(), checked.getCost());
    } */


   /* @Test
    public void deleteMovieWithNoRentals() {
        UUID movieId = UUID.randomUUID();
        given(rentRepository.findMovieById(movieId)).willReturn(false);
        given(movieRepository.findMovie(movieId)).willReturn(movie);
        boolean deleted = movieService.deleteMovie(movieId);

        assertTrue(deleted);
    } */

    @Test
    public void deleteMovieWithRentals() {
        UUID movieId = UUID.randomUUID();
        given(rentRepository.findMovieById(movieId)).willReturn(true);
        MovieException exception = assertThrows(MovieException.class, () -> movieService.deleteMovie(movieId));
        assertEquals(MovieMsg.MOVIE_IS_RENTED, exception.getMessage());
    }
    @Test
    public void deleteMovieNotExisted() {
        UUID movieId = UUID.randomUUID();
        given(rentRepository.findMovieById(movieId)).willReturn(false);
        given(movieRepository.findMovie(movieId)).willThrow(new MovieException(MovieMsg.MOVIE_NOT_FOUND) );
        MovieException exception = assertThrows(MovieException.class, () -> movieService.deleteMovie(movieId));
        assertEquals(MovieMsg.MOVIE_NOT_FOUND, exception.getMessage());
    }

}

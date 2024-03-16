package p.lodz.pl.pas2.servicesTest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import p.lodz.pl.pas2.exceptions.movieExceptions.*;
import p.lodz.pl.pas2.model.*;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.MovieRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.RentRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.RentRepository;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.servicesTest.TestMongoConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestMongoConfig.class,MovieService.class,RentService.class,MovieRepositoryMongoDB.class,RentRepositoryMongoDB.class})
@ActiveProfiles("test")
public class MovieServiceTest {

    @Autowired
    private  MovieService movieService;
    @Autowired
    private RentService rentService;
    static Movie movie = new Movie("test",34);
    private UUID movieId;

    @BeforeEach
    public void setUp() {
        movieService.addMovie(movie);
        movieId = movie.getId();
    }

    @Test
    @DirtiesContext
    public void getMovieById() {
        assertThat(movieService.getMovies().size()).isEqualTo(1);
        assertThat(movieService.getMovie(movieId).getId().toString()).isEqualTo(movieId.toString());
        assertThat(movieService.getMovie(movieId).getTitle()).isEqualTo(movie.getTitle());
        assertThat(movieService.getMovie(movieId).getCost()).isEqualTo(movie.getCost());
    }
    @Test
    @DirtiesContext
    public void getMovieByIdAndMovieNotExisted() {
        UUID nonExistentMovieId = UUID.randomUUID();
        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class, () -> {
            movieService.getMovie(nonExistentMovieId);
        });
        assertEquals(MovieMsg.MOVIE_NOT_FOUND, exception.getMessage());
    }
    @Test
    @DirtiesContext
    public void getMovies(){
        Movie movie2 = new Movie("test2",32);
        movieService.addMovie(movie2);
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        movies.add(movie2);
        assertThat(movieService.getMovies().get(0).getTitle().equals(movie.getTitle()));
        assertThat(movieService.getMovies().get(0).getCost()).isEqualTo(movie.getCost());
        assertThat(movieService.getMovies().get(1).getTitle().equals(movie2.getTitle()));
        assertThat(movieService.getMovies().get(1).getCost()).isEqualTo(movie2.getCost());
        assertThat(movieService.getMovies().size()).isEqualTo(2);
    }
    @Test
    @DirtiesContext
    public void getMoviesAndListIsEmpty(){
        movieService.deleteMovie(movieId);
        MoviesNotFoundException exception = assertThrows(MoviesNotFoundException.class, () -> {
            movieService.getMovies();
        });
        assertEquals(MovieMsg.MOVIES_NOT_FOUND, exception.getMessage());
    }
    @Test
    @DirtiesContext
    public void updateMovie(){
        Movie movie2 = new Movie("test2",32);
        movieService.updateMovie(movieId,movie2);
        assertThat(movieService.getMovie(movieId).getTitle()).isEqualTo(movie2.getTitle());
        assertThat(movieService.getMovie(movieId).getCost()).isEqualTo(movie2.getCost());
    }
    @Test
    @DirtiesContext
    public void updateMovieAndMovieNotExisted(){
        UUID nonExistentMovieId = UUID.randomUUID();
        Movie movie2 = new Movie("test2",32);
        ThereIsNoSuchMovieToUpdateException exception = assertThrows(ThereIsNoSuchMovieToUpdateException.class, () -> {
            movieService.updateMovie(nonExistentMovieId,movie2);
        });
        assertEquals(MovieMsg.MOVIE_NOT_FOUND, exception.getMessage());

    }
    @Test
    @DirtiesContext
    public void deleteMovieButMovieIsRented(){
        Client user = new Client(UUID.randomUUID(), "MaciekM", true, "Maciek", "Maciek", "1234");
        rentService.addRent(new Rent(user,movie, LocalDate.now()));
        MovieInUseException exception = assertThrows(MovieInUseException.class, () -> {
            movieService.deleteMovie(movie.getId());
        });
        assertEquals(MovieMsg.MOVIE_IS_RENTED, exception.getMessage());

    }
    @Test
    @DirtiesContext
    public void deleteMovieAndMovieNotExisted(){
        UUID nonExistentMovieId = UUID.randomUUID();
        ThereIsNoSuchMovieToDeleteException exception = assertThrows(ThereIsNoSuchMovieToDeleteException.class, () -> {
            movieService.deleteMovie(nonExistentMovieId);
        });
        assertEquals(MovieMsg.MOVIE_NOT_FOUND, exception.getMessage());
    }

}
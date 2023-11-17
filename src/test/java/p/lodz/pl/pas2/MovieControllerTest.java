package p.lodz.pl.pas2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.MovieController;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.services.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;

    @Test
    public void getMovies() throws Exception {
        Movie movie1 = new Movie("test1",40);
        Movie movie2 = new Movie("test2",69);
        List<Movie> movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);
        Mockito.when(movieService.getMovies()).thenReturn(movies);
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value(movie1.getTitle()))
                .andExpect(jsonPath("$[0].cost").value(movie1.getCost()))
                .andExpect(jsonPath("$[1].title").value(movie2.getTitle()))
                .andExpect(jsonPath("$[1].cost").value(movie2.getCost()));

    }


    @Test
    public void getMovieById() throws Exception {
        UUID movieId = UUID.randomUUID();
        Movie movie = new Movie("testMovie", 50);
        Mockito.when(movieService.getMovie(movieId)).thenReturn(movie);

        mockMvc.perform(get("/api/v1/movies/id/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.cost").value(movie.getCost()));
    }
    @Test
    public void updateMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        Movie movie = new Movie("testMovie", 50);
        movie.setId(movieId);
        Mockito.when(movieService.updateMovie(Mockito.any(),Mockito.any(Movie.class))).thenReturn(movie);
        mockMvc.perform(put("/api/v1/movies/id/{id}",movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movie)))
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.cost").value(movie.getCost()))
                .andExpect(jsonPath("$.id").value(movie.getId().toString()));

        Mockito.when(movieService.updateMovie(Mockito.any(),Mockito.any(Movie.class))).thenReturn(null);
        mockMvc.perform(put("/api/v1/movies/id/{id}",movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movie)))
                .andExpect(status().isNotFound());

    }

    @Test
    public void addMovie() throws Exception {
        Movie movie = new Movie("test", 25);
        Mockito.when(movieService.addMovie(Mockito.any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(movie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.cost").value(movie.getCost()))
                .andExpect(jsonPath("$.id").value(movie.getId()));

    }


    @Test
    public void deleteMovie() throws Exception {
        UUID movieId = UUID.randomUUID();
        Mockito.when(movieService.deleteMovie(movieId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/movies/id/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        Mockito.when(movieService.deleteMovie(movieId)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/movies/id/{id}", movieId))
                .andExpect(status().isBadRequest());

    }

    // Helper method to convert objects to JSON format
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

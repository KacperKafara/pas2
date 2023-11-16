package p.lodz.pl.pas2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.MovieController;
import p.lodz.pl.pas2.controllers.RentController;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.Request.RentRequest;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.model.UserType;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RentController.class)
public class RentControllerTest {
    @MockBean
    private MovieService movieService;
    @MockBean
    private UserService userService;
    @MockBean
    private RentService rentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addRent() throws Exception {
        UUID clientId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();

        User activeUser = new User("ActiveUser", UserType.CLIENT, true);
        User inactiveUser = new User("InactiveUser", UserType.CLIENT, false);
        Movie availableMovie = new Movie("AvailableMovie", 20);

        RentRequest rentRequest = new RentRequest(clientId, movieId, LocalDate.now());
        Rent rent = new Rent(activeUser,availableMovie,rentRequest.getStartDate());

        Mockito.when(userService.getUser(clientId)).thenReturn(activeUser);
        Mockito.when(movieService.getMovie(movieId)).thenReturn(availableMovie);
        Mockito.when(rentService.addRent(Mockito.any(Rent.class))).thenReturn(rent);

        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentRequest)))
                .andExpect(status().isCreated())
                //.andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.user.username").value(activeUser.getUsername()))
                .andExpect(jsonPath("$.user.userType").value(activeUser.getUserType().toString()))
                .andExpect(jsonPath("$.user.active").value(activeUser.isActive()))
                .andExpect(jsonPath("$.movie.title").value(availableMovie.getTitle()))
                .andExpect(jsonPath("$.movie.cost").value(availableMovie.getCost()))
                .andExpect(jsonPath("$.startDate").value(rentRequest.getStartDate().toString()));

        //uzytkownik nie istnieje
        Mockito.when(userService.getUser(clientId)).thenReturn(null);
        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User with this id does not exist"));
        //Uzytkownik nie aktywny
        Mockito.when(userService.getUser(clientId)).thenReturn(inactiveUser);
        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentRequest)))
                .andExpect(status().isLocked())
                .andExpect(content().string("User is not active"));

        // Film nie aktynwy
        Mockito.when(userService.getUser(clientId)).thenReturn(activeUser);
        Mockito.when(movieService.getMovie(movieId)).thenReturn(null);

        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with this id does not exist"));

        Mockito.when(userService.getUser(clientId)).thenReturn(activeUser);
        Mockito.when(movieService.getMovie(movieId)).thenReturn(availableMovie);
        Mockito.when(rentService.addRent(Mockito.any())).thenReturn(null);
        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentRequest)))
                .andExpect(content().string("Movie already rented or date is incorrect"));

    }

    private static String asJsonString(final Object obj) {
        try {

            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

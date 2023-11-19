package p.lodz.pl.pas2.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import p.lodz.pl.pas2.controllers.RentController;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentNotFoundException;
import p.lodz.pl.pas2.exceptions.rentExceptions.RentalStillOngoingException;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.request.RentRequest;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.msg.RentMsg;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        User activeUser = new Moderator("ActiveUser", true);
        User inactiveUser = new Moderator("InactiveUser", false);
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
                .andExpect(jsonPath("$.user.username").value(activeUser.getUsername()))
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
                .andExpect(content().string(UserMsg.USER_NOT_FOUND));
        //Uzytkownik nie aktywny
        Mockito.when(userService.getUser(clientId)).thenReturn(inactiveUser);
        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentRequest)))
                .andExpect(status().isLocked())
                .andExpect(content().string(UserMsg.USER_NOT_ACTIVE));

        // Film nie aktynwy
        Mockito.when(userService.getUser(clientId)).thenReturn(activeUser);
        Mockito.when(movieService.getMovie(movieId)).thenReturn(null);

        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(rentRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(MovieMsg.MOVIE_NOT_FOUND));
    }
    @Test

        public void getCurrentRents() throws Exception {
            Rent rent1 = new Rent(UUID.randomUUID(), new Moderator("user1", true), new Movie("movie1", 10), LocalDate.now(), null);
            Rent rent2 = new Rent(UUID.randomUUID(), new Moderator("user2", true), new Movie("movie2", 15), LocalDate.now(), null);
            List<Rent> currentRents = Arrays.asList(rent1, rent2);

            Mockito.when(rentService.getCurrentRents()).thenReturn(currentRents);

            mockMvc.perform(get("/api/v1/rents/current"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value(rent1.getId().toString()))
                    .andExpect(jsonPath("$[0].user.username").value(rent1.getUser().getUsername()))
                    .andExpect(jsonPath("$[0].user.active").value(rent1.getUser().isActive()))
                    .andExpect(jsonPath("$[0].movie.title").value(rent1.getMovie().getTitle()))
                    .andExpect(jsonPath("$[0].movie.cost").value(rent1.getMovie().getCost()))
                    .andExpect(jsonPath("$[0].startDate").value(rent1.getStartDate().toString()))
                    .andExpect(jsonPath("$[1].id").value(rent2.getId().toString()))
                    .andExpect(jsonPath("$[1].user.username").value(rent2.getUser().getUsername()))
                    .andExpect(jsonPath("$[1].user.active").value(rent2.getUser().isActive()))
                    .andExpect(jsonPath("$[1].movie.title").value(rent2.getMovie().getTitle()))
                    .andExpect(jsonPath("$[1].movie.cost").value(rent2.getMovie().getCost()))
                    .andExpect(jsonPath("$[1].startDate").value(rent2.getStartDate().toString()));
        }
    @Test
    public void getPastRents() throws Exception {
        Rent rent1 = new Rent(UUID.randomUUID(), new Moderator("user1", true), new Movie("movie1", 10), LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
        Rent rent2 = new Rent(UUID.randomUUID(), new Moderator("user2", true), new Movie("movie2", 15), LocalDate.now().minusDays(8), LocalDate.now().minusDays(2));
        List<Rent> pastRents = Arrays.asList(rent1, rent2);

        Mockito.when(rentService.getPastRents()).thenReturn(pastRents);

        mockMvc.perform(get("/api/v1/rents/past"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(rent1.getId().toString()))
                .andExpect(jsonPath("$[0].user.username").value(rent1.getUser().getUsername()))
                .andExpect(jsonPath("$[0].user.active").value(rent1.getUser().isActive()))
                .andExpect(jsonPath("$[0].movie.title").value(rent1.getMovie().getTitle()))
                .andExpect(jsonPath("$[0].movie.cost").value(rent1.getMovie().getCost()))
                .andExpect(jsonPath("$[0].startDate").value(rent1.getStartDate().toString()))
                .andExpect(jsonPath("$[0].endDate").value(rent1.getEndDate().toString()))
                .andExpect(jsonPath("$[1].id").value(rent2.getId().toString()))
                .andExpect(jsonPath("$[1].user.username").value(rent2.getUser().getUsername()))
                .andExpect(jsonPath("$[1].user.active").value(rent2.getUser().isActive()))
                .andExpect(jsonPath("$[1].movie.title").value(rent2.getMovie().getTitle()))
                .andExpect(jsonPath("$[1].movie.cost").value(rent2.getMovie().getCost()))
                .andExpect(jsonPath("$[1].startDate").value(rent2.getStartDate().toString()))
                .andExpect(jsonPath("$[1].endDate").value(rent2.getEndDate().toString()));
    }
    @Test
    public void deleteRent() throws Exception {
        UUID rentId = UUID.randomUUID();

        Mockito.when(rentService.deleteRent(rentId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/rents/{id}", rentId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        Mockito.when(rentService.deleteRent(rentId))
                .thenThrow(new RentalStillOngoingException(RentMsg.RENT_NOT_ENDED))
                .thenThrow(new RentNotFoundException(RentMsg.RENT_NOT_FOUND));
        mockMvc.perform(delete("/api/v1/rents/{id}", rentId))
                .andExpect(status().isLocked());

        mockMvc.perform(delete("/api/v1/rents/{id}", rentId))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void endRent_ValidEndDate() throws Exception {
        UUID rentId = UUID.randomUUID();
        LocalDate endDate = LocalDate.now().plusDays(5);
        Map<String, String> endDateMap = Collections.singletonMap("endDate", endDate.toString());

        Rent existingRent = new Rent(rentId, new Moderator("user", true), new Movie("movie", 10), LocalDate.now(), null);
        Rent updatedRent = new Rent(rentId, existingRent.getUser(), existingRent.getMovie(), existingRent.getStartDate(), endDate);

        Mockito.when(rentService.setEndTime(rentId, endDate)).thenReturn(updatedRent);

        mockMvc.perform(patch("/api/v1/rents/{id}", rentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(endDateMap)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedRent.getId().toString()))
                .andExpect(jsonPath("$.user.username").value(updatedRent.getUser().getUsername()))
                .andExpect(jsonPath("$.user.active").value(updatedRent.getUser().isActive()))
                .andExpect(jsonPath("$.movie.title").value(updatedRent.getMovie().getTitle()))
                .andExpect(jsonPath("$.movie.cost").value(updatedRent.getMovie().getCost()))
                .andExpect(jsonPath("$.startDate").value(updatedRent.getStartDate().toString()))
                .andExpect(jsonPath("$.endDate").value(updatedRent.getEndDate().toString()));
    }

    @Test
    public void invalidDate() throws Exception {
        UUID clientId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();

        // Creating an invalid RentRequest with a past start date
        RentRequest invalidRentRequest = new RentRequest(clientId, movieId, LocalDate.now().minusDays(1));

        mockMvc.perform(post("/api/v1/rents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidRentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("not valid due to validation error: "));
    }
    @Test
    public void badDateFormat() throws Exception {
        UUID rentId = UUID.randomUUID();
        Map<String, String> invalidEndDateMap = Collections.singletonMap("endDate", "invalid-date");

        mockMvc.perform(patch("/api/v1/rents/{id}",rentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidEndDateMap)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Incorrect date format"));
    }

    private static String asJsonString(final Object obj) {
        try {

            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package p.lodz.pl.pas2.servicesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import p.lodz.pl.pas2.exceptions.movieExceptions.MovieInUseException;
import p.lodz.pl.pas2.exceptions.rentExceptions.*;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotActiveException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.MovieMsg;
import p.lodz.pl.pas2.msg.RentMsg;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.MovieRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.RentRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.UserRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.UserRepository;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestMongoConfig.class, MovieService.class, RentService.class, UserService.class, UserRepositoryMongoDB.class, MovieRepositoryMongoDB.class, RentRepositoryMongoDB.class})
@ActiveProfiles("test")
public class RentServiceTest {
    @Autowired
    private RentService rentService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;
    static Movie movie = new Movie(UUID.randomUUID(), "test", 34);
    private User user = new Administrator(UUID.randomUUID(), "Bartosz", true);
    private Rent rent = new Rent(user, movie, LocalDate.now());
    UUID rentId;

    @BeforeEach
    @DirtiesContext
    public void setup() {
        rentService.addRent(rent);
        rentId = rentService.getCurrentRents().get(0).getId();
    }


    @Test
    @DirtiesContext
    public void getRentById() {
        assertEquals(rent.getId(), rentService.getRent(rentId).getId());
        assertEquals(rent.getUser().getId(), rentService.getRent(rentId).getUser().getId());
        assertEquals(rent.getMovie().getId(), rentService.getRent(rentId).getMovie().getId());
        assertThrows(RentNotFoundException.class, () -> {
            rentService.getRent(UUID.randomUUID());
        });
    }

    @Test
    @DirtiesContext
    public void addRentCorrectly() {
        Movie movie2 = new Movie(UUID.randomUUID(), "test", 34);
        User user2 = new Administrator(UUID.randomUUID(), "Mateusz", true);
        rentService.addRent(new Rent(user2, movie2, LocalDate.now()));
        assertThat(rentService.getCurrentRents().get(1).getMovie().getTitle()).isEqualTo(movie2.getTitle());
        assertThat(rentService.getCurrentRents().get(1).getMovie().getCost()).isEqualTo(movie2.getCost());
        assertThat(rentService.getCurrentRents().get(1).getUser().getUsername()).isEqualTo(user2.getUsername());
        assertThat(rentService.getCurrentRents().get(1).getUser().isActive()).isEqualTo(user2.isActive());
    }

    @Test
    @DirtiesContext
    public void addRentButUserNotActive() {
        User user2 = new Administrator(UUID.randomUUID(), "Mateusz", false);
        UserNotActiveException exception = assertThrows(UserNotActiveException.class, () -> {
            rentService.addRent(new Rent(user2, movie, LocalDate.now()));
        });
        assertEquals(UserMsg.USER_NOT_ACTIVE, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void addRentButWrongStartDate() {
        User user2 = new Administrator(UUID.randomUUID(), "Mateusz", true);
        StartDateException exception = assertThrows(StartDateException.class, () -> {
            rentService.addRent(new Rent(user2, movie, LocalDate.now().minusDays(4)));
        });
        assertEquals(RentMsg.WRONG_START_DATE, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void addRentButWrongEndDate() {
        User user2 = new Administrator(UUID.randomUUID(), "Mateusz", true);
        EndDateException exception = assertThrows(EndDateException.class, () -> {
            rentService.addRent(new Rent(user2, movie, LocalDate.now(), LocalDate.now().minusDays(4)));
        });
        assertEquals(RentMsg.WRONG_END_DATE, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void addRentButMovieRented() {
        MovieInUseException exception = assertThrows(MovieInUseException.class, () -> {
            rentService.addRent(new Rent(user, movie, LocalDate.now(), LocalDate.now()));
        });
        assertEquals(MovieMsg.MOVIE_IS_RENTED, exception.getMessage());
    }


    @Test
    @DirtiesContext
    public void deleteRent() {
        rentService.setEndTime(rentId,LocalDate.now());
        assertThat(rentService.deleteRent(rentId)).isTrue();
    }

    @Test
    @DirtiesContext
    public void deleteRentNotFound() {
        UUID nonExistentRentId = UUID.randomUUID();
        ThereIsNoSuchRentToDelete exception = assertThrows(ThereIsNoSuchRentToDelete.class, () -> {
            rentService.deleteRent(nonExistentRentId);
        });
        assertEquals(RentMsg.RENT_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void deleteRentStillOngoing() {
        rent.setEndDate(LocalDate.now().plusDays(1));
        RentalStillOngoingException exception = assertThrows(RentalStillOngoingException.class, () -> {
            rentService.deleteRent(rentId);
        });
        assertEquals(RentMsg.RENT_NOT_ENDED, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void setEndTime() {
        LocalDate newEndTime = LocalDate.now().plusDays(2);
        Rent updatedRent = rentService.setEndTime(rentId, newEndTime);
        assertThat(updatedRent.getEndDate()).isEqualTo(newEndTime);
        assertThrows(ThereIsNoSuchRentToUpdateException.class,
                () -> {
                    rentService.setEndTime(UUID.randomUUID(), newEndTime);
                });
    }

    @Test
    @DirtiesContext
    public void setEndTimeInvalid() {
        LocalDate newEndTime = LocalDate.now().minusDays(1);
        EndDateException exception = assertThrows(EndDateException.class, () -> {
            rentService.setEndTime(rentId, newEndTime);
        });
        assertEquals(RentMsg.WRONG_END_DATE, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void getCurrentRents() {
        List<Rent> currentRents = rentService.getCurrentRents();
        assertThat(currentRents).hasSize(1);
        assertThat(currentRents.get(0).getId()).isEqualTo(rentId);
    }

    @Test
    @DirtiesContext
    public void getCurrentRentsNotFound() {
        rentService.setEndTime(rentId,LocalDate.now());
        RentsNotFoundException exception = assertThrows(RentsNotFoundException.class, () -> {
            rentService.getCurrentRents();
        });
        assertEquals(exception.getMessage(),RentMsg.RENTS_NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void getPastRents() {
        rentService.setEndTime(rentId,LocalDate.now());
        List<Rent> pastRents = rentService.getPastRents();
        assertThat(pastRents.size()).isEqualTo(1);
    }

    @Test
    @DirtiesContext
    public void getPastRentsNotFound() {
        rent.setEndDate(LocalDate.now().plusDays(1));
        RentsNotFoundException exception = assertThrows(RentsNotFoundException.class, () -> {
            rentService.getPastRents();
        });
        assertEquals(RentMsg.RENTS_NOT_FOUND, exception.getMessage());
    }
}




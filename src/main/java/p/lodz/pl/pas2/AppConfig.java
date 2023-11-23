package p.lodz.pl.pas2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import p.lodz.pl.pas2.model.*;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;

@Configuration
public class AppConfig {
    @Bean
    public static CommandLineRunner commandLineRunner(MovieService movieService,
                                                      RentService rentService,
                                                      UserService userService) {
        return args -> {
            movieService.addMovie(new Movie("Saw", 10));
            movieService.addMovie(new Movie("Saw II", 20));
            movieService.addMovie(new Movie("Saw III", 30));
            movieService.addMovie(new Movie("Saw IV", 14));
            movieService.addMovie(new Movie("Saw V", 19));
            userService.addUser(new Moderator("Maciek", true));
            userService.addUser(new Administrator("Jacek", true));
            userService.addUser(new Client("Kuba", false, "Kuba", "Aaa"));
            rentService.addRent(new Rent(userService.getUser("Maciek"), movieService.getMovies().get(0), LocalDate.now(), LocalDate.now().plusDays(5)));
            rentService.addRent(new Rent(userService.getUser("Maciek"), movieService.getMovies().get(1), LocalDate.now(), LocalDate.now().plusDays(5)));
            rentService.addRent(new Rent(userService.getUser("Maciek"), movieService.getMovies().get(3), LocalDate.now(), LocalDate.now()));
            rentService.addRent(new Rent(userService.getUser("Jacek"), movieService.getMovies().get(2)));
        };
    }
}

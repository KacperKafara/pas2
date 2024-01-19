package p.lodz.pl.pas2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import p.lodz.pl.pas2.Dto.LoginDto;
import p.lodz.pl.pas2.model.*;
import p.lodz.pl.pas2.repositories.MovieRepository;
import p.lodz.pl.pas2.repositories.RentRepository;
import p.lodz.pl.pas2.repositories.UserRepository;
import p.lodz.pl.pas2.services.AuthenticationService;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;

@Configuration
public class AppConfig {
    @Bean
    public static CommandLineRunner commandLineRunner(@Qualifier("movieRepositoryMongoDB") MovieRepository movieRepository,
                                                      @Qualifier("rentRepositoryMongoDB") RentRepository rentRepository,
                                                      @Qualifier("userRepositoryMongoDB") UserRepository userRepository,
                                                      AuthenticationService authenticationService) {
        return args -> {
            movieRepository.saveMovie(new Movie("Saw", 10));
            movieRepository.saveMovie(new Movie("Saw II", 20));
            movieRepository.saveMovie(new Movie("Saw III", 30));
            movieRepository.saveMovie(new Movie("Saw IV", 14));
            movieRepository.saveMovie(new Movie("Saw V", 19));
            userRepository.saveClient(new Client("client1", true, "Maciek", "M", "$2b$12$6J4h6z.Er73Ud7zWhUT4yueCCFl2xCLkUZGHi8JtJYYwxp3NHtbBK"));
            userRepository.saveClient(new Client("client2", false, "Kuba", "Aaa","$2b$12$6J4h6z.Er73Ud7zWhUT4yueCCFl2xCLkUZGHi8JtJYYwxp3NHtbBK"));
            userRepository.saveClient(new Moderator("HubertH", false, "$2b$12$6J4h6z.Er73Ud7zWhUT4yueCCFl2xCLkUZGHi8JtJYYwxp3NHtbBK"));
            userRepository.saveClient(new Administrator("admin", true, "$2b$12$6J4h6z.Er73Ud7zWhUT4yueCCFl2xCLkUZGHi8JtJYYwxp3NHtbBK"));
            rentRepository.saveRent(new Rent((Client) userRepository.findUser("client1"), movieRepository.findMovies().get(0), LocalDate.now(), LocalDate.now().plusDays(5)));
            rentRepository.saveRent(new Rent((Client) userRepository.findUser("client1"), movieRepository.findMovies().get(1), LocalDate.now().minusDays(5), LocalDate.now().minusDays(1)));
            rentRepository.saveRent(new Rent((Client) userRepository.findUser("client1"), movieRepository.findMovies().get(3), LocalDate.now().minusDays(5)));
            rentRepository.saveRent(new Rent((Client) userRepository.findUser("client2"), movieRepository.findMovies().get(2)));
        };
    }
}

package p.lodz.pl.pas2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import p.lodz.pl.pas2.controllers.MovieController;
import p.lodz.pl.pas2.controllers.RentController;
import p.lodz.pl.pas2.controllers.UserController;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.model.UserType;
import p.lodz.pl.pas2.services.MovieService;
import p.lodz.pl.pas2.services.RentService;
import p.lodz.pl.pas2.services.UserService;

import java.time.LocalDate;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class Pas2Application {

    public static void main(String[] args) {
        SpringApplication.run(Pas2Application.class, args);
    }



}

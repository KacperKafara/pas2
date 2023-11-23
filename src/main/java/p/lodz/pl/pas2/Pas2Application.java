package p.lodz.pl.pas2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class Pas2Application {

    public static void main(String[] args) {
        SpringApplication.run(Pas2Application.class, args);
    }



}

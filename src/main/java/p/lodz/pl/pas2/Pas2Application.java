package p.lodz.pl.pas2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Import;
import p.lodz.pl.pas2.repositories.AbstractMongoRepositoryConfig;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class Pas2Application {

    public static void main(String[] args) {
        SpringApplication.run(Pas2Application.class, args);
    }



}

package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Rent {
    @BsonId
    private UUID id;
    @BsonProperty("user")
    private User user;
    @BsonProperty("movie")
    private Movie movie;
    @BsonProperty("start_date")
    private LocalDate startDate;
    @BsonProperty("end_date")
    private LocalDate endDate;

    @BsonCreator
    public Rent(@BsonId UUID id,
                @BsonProperty("user") User user,
                @BsonProperty("movie") Movie movie,
                @BsonProperty("start_date") LocalDate startDate,
                @BsonProperty("end_date") LocalDate endDate) {

        this.id = id;
        this.user = user;
        this.movie = movie;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Rent(User user, Movie movie, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.movie = movie;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Rent(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
        startDate = LocalDate.now();
        endDate = null;
    }
}
package p.lodz.pl.pas2.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @NotBlank
    @BsonProperty("user")
    private User user;
    @NotNull
    @NotBlank
    @BsonProperty("movie")
    private Movie movie;
    @NotNull
    @FutureOrPresent
    @BsonProperty("start_date")
    private LocalDate startDate;
    @FutureOrPresent
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

    public Rent(User user, Movie movie, LocalDate startDate) {
        this.user = user;
        this.movie = movie;
        this.startDate = startDate;
        endDate = null;
    }
}
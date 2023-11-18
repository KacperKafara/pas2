package p.lodz.pl.pas2.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import p.lodz.pl.pas2.model.Movie;
import p.lodz.pl.pas2.model.Rent;
import p.lodz.pl.pas2.model.User;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class RentRequest {

    @NotNull
    private UUID clientID;

    @NotNull
    private UUID movieID;

    @FutureOrPresent
    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    RentRequest(){}

    public RentRequest(UUID clientID, UUID movieID, LocalDate startDate) {
        this.clientID = clientID;
        this.movieID = movieID;
        this.startDate = startDate;
    }

    public static Rent rentRequestToRent(User user, Movie movie, LocalDate startDate) {
        return new Rent(user, movie, startDate);
    }
}

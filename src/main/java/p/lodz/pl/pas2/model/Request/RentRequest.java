package p.lodz.pl.pas2.model.Request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class RentRequest {

    @NotNull
    UUID clientID;
    @NotNull
    UUID movieID;
    @FutureOrPresent
    @NotNull
    LocalDate startDate;
    LocalDate endDate;
    public RentRequest(){

    }

    public RentRequest(UUID clientID, UUID movieID, LocalDate startDate) {
        this.clientID = clientID;
        this.movieID = movieID;
        this.startDate = startDate;
    }
}

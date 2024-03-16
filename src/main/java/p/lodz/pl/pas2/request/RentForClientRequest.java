package p.lodz.pl.pas2.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RentForClientRequest {

    @NotNull(message = "movie's id not given")
    private UUID movieID;

    @FutureOrPresent(message = "start date cannot be past date")
    @NotNull(message = "start date not given")
    private LocalDate startDate;

    private LocalDate endDate;

    public RentForClientRequest(UUID movieID, LocalDate startDate) {
        this.movieID = movieID;
        this.startDate = startDate;
    }
}

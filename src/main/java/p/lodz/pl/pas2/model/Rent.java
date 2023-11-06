package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Rent {
    private UUID id;
    private User user;
    private Movie movie;
    private LocalDate startDate;
    private LocalDate endDate;

    public Rent(User user, Movie movie, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.movie = movie;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
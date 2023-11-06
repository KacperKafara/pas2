package p.lodz.pl.pas2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class Rent {
    private UUID id;
    private Client client;
    private Movie movie;
    private LocalDate startDate;
    private LocalDate endDate;

    public Rent(Client client, Movie movie, LocalDate startDate, LocalDate endDate) {
        this.client = client;
        this.movie = movie;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
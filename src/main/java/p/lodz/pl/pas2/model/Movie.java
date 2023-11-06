package p.lodz.pl.pas2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Movie {
    private UUID id;
    private String title;
    private double cost;

    public Movie(String title, double cost) {
        this.title = title;
        this.cost = cost;
    }
}
package p.lodz.pl.pas2.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @BsonId
    private UUID id;

    @BsonProperty("title")
    private String title;

    @BsonProperty("cost")
    private double cost;

    @BsonCreator
    public Movie(
            @BsonId
            UUID id,
            @BsonProperty("title")
            String title,
            @BsonProperty("cost")
            double cost
    ){
        this.id = id;
        this.title = title;
        this.cost = cost;
    }

    public Movie(String title, double cost) {
        this.title = title;
        this.cost = cost;
    }
}
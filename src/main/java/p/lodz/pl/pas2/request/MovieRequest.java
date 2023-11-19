package p.lodz.pl.pas2.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonProperty;
import p.lodz.pl.pas2.model.Movie;

@AllArgsConstructor
@Getter
public class MovieRequest {

    @NotBlank
    @NotNull
    private String title;

    @NotNull
    @Min(0)
    private double cost;
}

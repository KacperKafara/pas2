package p.lodz.pl.pas2.model.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class RentRequest {
    UUID clientID;
    UUID movieID;
    LocalDate startDate;
    LocalDate endDate;
}

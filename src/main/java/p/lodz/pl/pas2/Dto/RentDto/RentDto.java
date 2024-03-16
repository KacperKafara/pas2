package p.lodz.pl.pas2.Dto.RentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import p.lodz.pl.pas2.Dto.UserDto.ClientDto;
import p.lodz.pl.pas2.model.Movie;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class RentDto {
    private final UUID id;
    private final ClientDto client;
    private final Movie movie;
    private final LocalDate startDate;
    private final LocalDate endDate;
}

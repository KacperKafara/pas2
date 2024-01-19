package p.lodz.pl.pas2.Dto.RentDto;

import org.springframework.stereotype.Component;
import p.lodz.pl.pas2.Dto.UserDto.UserDtoMapper;
import p.lodz.pl.pas2.model.Rent;

import java.util.ArrayList;
import java.util.List;

@Component
public class RentDtoMapper {
    public RentDto rentToRentDto(Rent rent) {
        return new RentDto(
                rent.getId(),
                new UserDtoMapper().clientToUserDto(rent.getUser()),
                rent.getMovie(),
                rent.getStartDate(),
                rent.getEndDate()
        );
    }

    public List<RentDto> rentsToRentsDto(List<Rent> rents) {
        List<RentDto> rentDtos = new ArrayList<>();
        for (Rent rent : rents) {
            rentDtos.add(rentToRentDto(rent));
        }
        return rentDtos;
    }
}

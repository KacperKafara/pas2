package p.lodz.pl.pas2.Dto.UserDto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ClientDto extends UserDto {
    private final String firstName;
    private final String lastName;
    public ClientDto(UUID id, String login, String userType, String firstName, String lastName, boolean active) {
        super(id, login, userType, active);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

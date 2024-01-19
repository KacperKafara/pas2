package p.lodz.pl.pas2.Dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ClientDto extends UserDto {
    private final String firstName;
    private final String lastName;
    public ClientDto(UUID id, String login, String userType, String firstName, String lastName) {
        super(id, login, userType);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

package p.lodz.pl.pas2.Dto.UserDto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoginDto {
    private UUID id;
    private String login;
    private String token;
    private String userType;

    public LoginDto(UUID id, String login, String userType) {
        this.id = id;
        this.login = login;
        this.userType = userType;
    }
}

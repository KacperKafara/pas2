package p.lodz.pl.pas2.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public abstract class UserDto {
    protected UUID id;
    protected String login;
    protected String token;
    protected String  userType;

    public UserDto(UUID id, String login, String userType) {
        this.id = id;
        this.login = login;
        this.userType = userType;
    }
}

package p.lodz.pl.pas2.Dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
@Getter
@ToString
public class UserDto {
    private UUID id;
    private String email;
    private String login;
    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    private String  userType;

    public UserDto(UUID id, String email, String login,String  userType) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.userType = userType;
    }
}

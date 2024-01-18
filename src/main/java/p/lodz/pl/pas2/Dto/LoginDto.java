package p.lodz.pl.pas2.Dto;

import lombok.Getter;

@Getter
public class LoginDto {
    private String login;
    private String password;

    public LoginDto(String login, String password) {
        this.login = login;
        this.password = password;
    }
}

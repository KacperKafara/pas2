package p.lodz.pl.pas2.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String login;
    private String password;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}

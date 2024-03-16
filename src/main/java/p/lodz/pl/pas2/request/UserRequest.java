package p.lodz.pl.pas2.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public abstract class UserRequest {
    @NotNull(message = "username not given")
    @NotBlank(message = "username cannot be empty")
    protected String username;
    protected boolean active;
    protected String password;

    public UserRequest(String username, boolean active, String password) {
        this.username = username;
        this.active = active;
        this.password = password;
    }
}

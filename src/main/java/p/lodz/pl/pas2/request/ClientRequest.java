package p.lodz.pl.pas2.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClientRequest extends UserRequest {

    @NotNull(message = "first name not given")
    @NotBlank(message = "first name cannot be empty")
    private final String firstName;

    @NotNull(message = "last name not given")
    @NotBlank(message = "last name cannot be empty")
    private final String lastName;
    public ClientRequest(String username, boolean active, String firstName, String lastName, String password) {
        super(username, active, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

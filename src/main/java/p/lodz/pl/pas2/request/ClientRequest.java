package p.lodz.pl.pas2.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClientRequest extends UserRequest {

    @NotNull
    @NotBlank
    private final String firstName;

    @NotNull
    @NotBlank
    private final String lastName;
    public ClientRequest(String username, boolean active, String firstName, String lastName) {
        super(username, active);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
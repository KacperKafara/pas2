package p.lodz.pl.pas2.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID id;
    private String userName;
    private UserType userType;
    private boolean active;

    public User(String username, UserType userType, boolean active) {
        this.userName = username;
        this.userType = userType;
        this.active = active;
    }


}

package p.lodz.pl.pas2.Dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public abstract class UserDto {
    protected UUID id;
    protected String username;
    protected String userType;
    protected boolean active;
}

package p.lodz.pl.pas2.Dto.UserDto;

import java.util.UUID;

public class ModeratorDto extends UserDto{
    public ModeratorDto(UUID id, String login, String userType, boolean active) {
        super(id, login, userType, active);
    }
}

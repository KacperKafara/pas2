package p.lodz.pl.pas2.Dto;

import java.util.UUID;

public class ModeratorDto extends UserDto{
    public ModeratorDto(UUID id, String login, String userType) {
        super(id, login, userType);
    }
}

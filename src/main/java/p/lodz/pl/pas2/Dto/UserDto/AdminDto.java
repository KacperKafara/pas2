package p.lodz.pl.pas2.Dto.UserDto;

import java.util.UUID;

public class AdminDto extends UserDto{
    public AdminDto(UUID id, String login, String userType, boolean active) {
        super(id, login, userType, active);
    }
}

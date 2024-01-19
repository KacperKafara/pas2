package p.lodz.pl.pas2.Dto;

import java.util.UUID;

public class AdminDto extends UserDto{
    public AdminDto(UUID id, String login, String userType) {
        super(id, login, userType);
    }
}

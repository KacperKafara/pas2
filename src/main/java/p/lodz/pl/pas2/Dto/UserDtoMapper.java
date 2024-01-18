package p.lodz.pl.pas2.Dto;

import org.springframework.stereotype.Component;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;

@Component
public class UserDtoMapper {

    public UserDto clientToUserDto(Client user) {
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getClass().getSimpleName().toLowerCase());
    }
    public UserDto adminToUserDto(Administrator user) {
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getClass().getSimpleName().toLowerCase());
    }
    public UserDto moderatorToUserDto(Moderator user) {
        return new UserDto(user.getId(), user.getEmail(), user.getUsername(), user.getClass().getSimpleName().toLowerCase());
    }
}

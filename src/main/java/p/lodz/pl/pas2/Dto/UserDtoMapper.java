package p.lodz.pl.pas2.Dto;

import org.springframework.stereotype.Component;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;

@Component
public class UserDtoMapper {

    public ClientDto clientToUserDto(Client user) {
        return new ClientDto(user.getId(), user.getUsername(), user.getClass().getSimpleName().toLowerCase(), user.getFirstName(), user.getLastName());
    }
    public AdminDto adminToUserDto(Administrator user) {
        return new AdminDto(user.getId(), user.getUsername(), user.getClass().getSimpleName().toLowerCase());
    }
    public ModeratorDto moderatorToUserDto(Moderator user) {
        return new ModeratorDto(user.getId(), user.getUsername(), user.getClass().getSimpleName().toLowerCase());
    }
}

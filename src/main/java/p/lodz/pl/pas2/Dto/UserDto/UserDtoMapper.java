package p.lodz.pl.pas2.Dto.UserDto;

import org.springframework.stereotype.Component;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDtoMapper {

    public ClientDto clientToUserDto(Client user) {
        return new ClientDto(user.getId(), user.getUsername(), user.getClass().getSimpleName().toUpperCase(), user.getFirstName(), user.getLastName(), user.isActive());
    }
    public AdminDto adminToUserDto(Administrator user) {
        return new AdminDto(user.getId(), user.getUsername(), user.getClass().getSimpleName().toUpperCase(), user.isActive());
    }
    public ModeratorDto moderatorToUserDto(Moderator user) {
        return new ModeratorDto(user.getId(), user.getUsername(), user.getClass().getSimpleName().toUpperCase(), user.isActive());
    }

    public List<UserDto> adminsToUserDtos(List<User> administrators) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : administrators) {
            userDtos.add(adminToUserDto((Administrator) user));
        }
        return userDtos;
    }

    public List<UserDto> clientsToUserDtos(List<User> clients) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : clients) {
            userDtos.add(clientToUserDto((Client) user));
        }
        return userDtos;
    }

    public List<UserDto> moderatorsToUserDtos(List<User> moderators) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : moderators) {
            userDtos.add(moderatorToUserDto((Moderator) user));
        }
        return userDtos;
    }

    public UserDto userToUserDto(User user) {
        if (user instanceof Administrator) {
            return adminToUserDto((Administrator) user);
        } else if (user instanceof Client) {
            return clientToUserDto((Client) user);
        } else if (user instanceof Moderator) {
            return moderatorToUserDto((Moderator) user);
        }
        return null;
    }

    public List<UserDto> usersToUserDtos(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(userToUserDto(user));
        }
        return userDtos;
    }
}

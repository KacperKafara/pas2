package p.lodz.pl.pas2.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.Dto.LoginDto;
import p.lodz.pl.pas2.Dto.UserDto;
import p.lodz.pl.pas2.Dto.UserDtoMapper;
import p.lodz.pl.pas2.exceptions.AuthenticationExceptions.InvalidPasswordException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotActiveException;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.Moderator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoMapper userDtoMapper;

    public UserDto loginUser(LoginDto user) {
        User foundUser = userService.getUserByLogin(user.getLogin());

        if(!foundUser.isActive()) {
            throw new UserNotActiveException(UserMsg.USER_NOT_ACTIVE);
        }

        UserDto userDto;
        if (foundUser.getClass().getSimpleName().equalsIgnoreCase("client")) {
            userDto = userDtoMapper.clientToUserDto((Client) foundUser);
        } else if (foundUser.getClass().getSimpleName().equalsIgnoreCase("administrator")) {
            userDto = userDtoMapper.adminToUserDto((Administrator) foundUser);
        } else {
            userDto = userDtoMapper.moderatorToUserDto((Moderator) foundUser);
        }

        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return userDto;
        }
        throw new InvalidPasswordException("Wrong password");
    }

}

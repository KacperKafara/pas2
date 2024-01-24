package p.lodz.pl.pas2.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.Dto.UserDto.ClientDto;
import p.lodz.pl.pas2.Dto.UserDto.LoginDto;
import p.lodz.pl.pas2.request.ClientRequest;
import p.lodz.pl.pas2.request.LoginRequest;
import p.lodz.pl.pas2.Dto.UserDto.UserDto;
import p.lodz.pl.pas2.Dto.UserDto.UserDtoMapper;
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

    public LoginDto loginUser(LoginRequest user) {
        User foundUser = userService.getUser(user.getLogin());

        if (!foundUser.isActive()) {
            throw new UserNotActiveException(UserMsg.USER_NOT_ACTIVE);
        }

        LoginDto loginDto = new LoginDto(foundUser.getId(), foundUser.getUsername(), foundUser.getClass().getSimpleName().toUpperCase());
        if(foundUser instanceof Client) {
            loginDto.setFirstName(((Client) foundUser).getFirstName());
            loginDto.setLastName(((Client) foundUser).getLastName());
        }

        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            return loginDto;
        }
        throw new InvalidPasswordException("Wrong password");
    }

    public UserDto registerUser(ClientRequest user) {
        User newUser = new Client(user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName(), user.getPassword());
        User addedUser = userService.addUser(newUser);
        return userDtoMapper.clientToUserDto((Client) addedUser);
    }

}

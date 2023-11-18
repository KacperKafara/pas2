package p.lodz.pl.pas2.servicesTest;


import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.UserRepository;
import p.lodz.pl.pas2.services.UserService;

import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.UUID;
/*
@ExtendWith(MockitoExtension.class)
public class UserServiceTest{
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    User user;
    UUID userId;

    @BeforeEach
    public void setup(){
        user = new User("Matesz", UserType.CLIENT, true);
        userId = UUID.randomUUID();
        //user.setId(userId);
    }

    @Test
    public void getUserByIdADnUserExist() {
        given(userRepository.findUser(userId)).willReturn(user);
        User checked = userService.getUser(userId);
        assertThat(checked.getUsername()).isEqualTo(user.getUsername());
        assertThat(checked.isActive()).isEqualTo(true);
        assertThat(checked.getId()).isEqualTo(user.getId());
    }
    @Test
    public void getUserByNicknameAndUserExist(){
        given(userRepository.findUser(user.getUsername())).willReturn(user);
        User checked = userService.getUser(user.getUsername());
        assertThat(checked.getUsername()).isEqualTo(user.getUsername());
        assertThat(checked.isActive()).isEqualTo(true);
        assertThat(checked.getId()).isEqualTo(user.getId());
    }
    @Test
        public void getUsers() {
        User user2 =new User("Test",UserType.CLIENT,true);
        given(userRepository.findUsers()).willReturn(List.of(user,user2));
        List<User>users = userService.getUsers();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0).getUsername()).isEqualTo(user.getUsername());
        assertThat(users.get(0).isActive()).isEqualTo(user.isActive());
        assertThat(users.get(1).getUsername()).isEqualTo(user2.getUsername());
        assertThat(users.get(1).isActive()).isEqualTo(user2.isActive());
    }
    @Test
    public void addUserAndLoginNotUsed(){
        given(userRepository.findUser(user.getUsername())).willReturn(null);
        given(userRepository.saveClient(user)).willReturn(user);
        User checked = userService.addUser(user);
        assertThat(checked.getUsername()).isEqualTo(user.getUsername());
    }
    @Test
    public void addUserAndLoginUsed() throws InstantiationException, IllegalAccessException {
        given(userRepository.findUser(user.getUsername())).willReturn(User.class.newInstance());
        UsernameInUse exception = assertThrows(UsernameInUse.class, () -> userService.addUser(user));
        assertEquals(UserMsg.USERNAME_IN_USE, exception.getMessage());
    }
    @Test
    public void setActiveAndUserExist(){
        given(userRepository.findUser(userId)).willReturn(user);
        User checked = userService.setActive(userId,false);
        assertThat(checked.getUsername()).isEqualTo(user.getUsername());
        assertThat(checked.isActive()).isEqualTo(false);
    }
    @Test
    public void setActiveAndUserNotExist(){
        given(userRepository.findUser(user.getId())).willReturn(null);
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.setActive(user.getId(),true));
        assertEquals(UserMsg.USER_NOT_FOUND, exception.getMessage());
    }
    @Test
    public void updateUserAndUserExist(){
        given(userRepository.findUser(user.getId())).willReturn(user);
        user.setUsername("Mateusz");
        user.setActive(false);
        given(userRepository.updateUser(user.getId(),user)).willReturn(user);
        User checked = userService.updateUser(user.getId(),user);
        assertThat(checked.getUsername()).isEqualTo(user.getUsername());
        assertThat(checked.isActive()).isEqualTo(false);
        assertThat(checked.getId()).isEqualTo(user.getId());
    }
    @Test
    public void updateUserAndUserNotExist(){
        given(userRepository.findUser(user.getId())).willReturn(null);
        UserNotFound exception = assertThrows(UserNotFound.class, () -> userService.updateUser(user.getId(),user));
        assertEquals(UserMsg.USER_NOT_FOUND, exception.getMessage());
    }



}*/

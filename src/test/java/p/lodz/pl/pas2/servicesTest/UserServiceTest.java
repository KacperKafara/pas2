package p.lodz.pl.pas2.servicesTest;

import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsersNotFoundException;
import p.lodz.pl.pas2.model.Administrator;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.MovieRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.RentRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.UserRepositoryMongoDB;
import p.lodz.pl.pas2.services.UserService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestMongoConfig.class, UserService.class, UserRepositoryMongoDB.class})
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;
    User user = new Administrator("Bartosz",true);
    private UUID userId;

    @BeforeEach
    public void setUp() {
        userService.addUser(user);
        userId = user.getId();
    }

    @Test
    @DirtiesContext
    public void getUserById() {
        assertThat(userService.getUsers().size()).isEqualTo(1);
        assertThat(userService.getUser(userId).getId().toString()).isEqualTo(userId.toString());
        assertThat(userService.getUser(userId).getUsername()).isEqualTo(user.getUsername());
        assertThat(userService.getUser(userId).isActive()).isEqualTo(user.isActive());
    }

    @Test
    @DirtiesContext
    public void getUserByUsername() {
        assertThat(userService.getUser(user.getUsername()).getId().toString()).isEqualTo(userId.toString());
        assertThat(userService.getUser(user.getUsername()).getUsername()).isEqualTo(user.getUsername());
        assertThat(userService.getUser(user.getUsername()).isActive()).isEqualTo(user.isActive());
    }

    @Test
    @DirtiesContext
    public void getUsers() {
        User user2 = new Administrator("John", false);
        userService.addUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        assertThat(userService.getUsers().get(0).getUsername().equals(user.getUsername()));
        assertThat(userService.getUsers().get(0).isActive()).isEqualTo(user.isActive());
        assertThat(userService.getUsers().get(1).getUsername().equals(user2.getUsername()));
        assertThat(userService.getUsers().get(1).isActive()).isEqualTo(user2.isActive());
        assertThat(userService.getUsers().size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void getUsersByPattern() {
        User user2 = new Administrator("Bartek", false);
        userService.addUser(user2);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);
        assertThat(userService.getUsersByPattern("Bar").size()).isEqualTo(2);
        assertThat(userService.getUsersByPattern("Bart").size()).isEqualTo(2);
        UsersNotFoundException exception = assertThrows(UsersNotFoundException.class, () -> {
            userService.getUsersByPattern("xyz");
        });
        assertEquals(UserMsg.USERS_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void addUser() {
        User newUser = new Administrator("Alice", true);
        userService.addUser(newUser);
        assertThat(userService.getUsers().size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void addUserWithDuplicateUsername() {
        User duplicateUser = new Administrator(user.getUsername(), false);
        UsernameInUseException exception = assertThrows(UsernameInUseException.class, () -> {
            userService.addUser(duplicateUser);
        });
        assertEquals(UserMsg.USERNAME_IN_USE, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void setActiveForExistingUser() {
         userService.setActive(userId, false);
        assertFalse(userService.getUser(userId).isActive());
    }

    @Test
    @DirtiesContext
    public void setActiveForNonexistentUser() {
        UUID nonExistentUserId = UUID.randomUUID();
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.setActive(nonExistentUserId, true);
        });
        assertEquals(UserMsg.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DirtiesContext
    public void updateUserForExistingUser() {
        User updatedUser = new Administrator("UpdatedName", false);
        userService.updateUser(userId, updatedUser);
        assertThat(userService.getUser(userId).getUsername()).isEqualTo(updatedUser.getUsername());
        assertThat(userService.getUser(userId).isActive()).isEqualTo(updatedUser.isActive());
    }

    @Test
    @DirtiesContext
    public void updateUserForNonexistentUser() {
        UUID nonExistentUserId = UUID.randomUUID();
        User updatedUser = new Administrator("UpdatedName", false);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(nonExistentUserId, updatedUser);
        });
        assertEquals(UserMsg.USER_NOT_FOUND, exception.getMessage());
    }


}

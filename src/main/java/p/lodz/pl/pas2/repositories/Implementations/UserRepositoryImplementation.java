package p.lodz.pl.pas2.repositories.Implementations;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
@Scope("singleton")
public class UserRepositoryImplementation implements UserRepository {

    private final List<User> users;
    public UserRepositoryImplementation() {
        users = new CopyOnWriteArrayList<>();

    }

    @Override
    public User findUser(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findUsersMatchToValue(String username) {
        return null;
    }

    @Override
    public User saveClient(User user) {
        if(isUsernameUnique(user.getUsername())) {
            user.setId(UUID.randomUUID());
            users.add(user);
            return user;
        } else {
            throw new UsernameInUseException(UserMsg.USERNAME_IN_USE);
        }
    }

    @Override
    public List<User> findUsers() {
        return users;
    }

    @Override
    public User setActive(UUID id, boolean active) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                user.setActive(active);
                return user;
            }
        }
        return null;
    }

    @Override
    public User updateUser(UUID id, User user) {
        User updatedUser = findUser(id);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setActive(user.isActive());
        return updatedUser;
    }

    private boolean isUsernameUnique(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }
}
package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.UserRepository;
import p.lodz.pl.pas2.model.User;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    @Autowired
    public UserService(@Qualifier("userRepositoryMongoDB") UserRepository repository) {
        this.repository = repository;
    }

    public User getUser(UUID id) {
        User user = repository.findUser(id);
        if (user == null) throw new UserNotFoundException(UserMsg.USER_NOT_FOUND);
        return repository.findUser(id);
    }

    public User getUser(String username) {
        User user = repository.findUser(username);
        if (user == null) throw new UserNotFoundException(UserMsg.USER_NOT_FOUND);
        return repository.findUser(username);
    }

    public List<User> getUsers() {
        List<User> users = repository.findUsers();
        if(users.isEmpty()) throw new UserNotFoundException(UserMsg.USERS_NOT_FOUND);
        return repository.findUsers();
    }

    public List<User> getUsersByPattern(String pattern) {
        List<User> users = repository.findUsersMatchToValue(pattern);
        if(users.isEmpty()) throw new UserNotFoundException(UserMsg.USERS_NOT_FOUND);
        return repository.findUsers();
    }

    public User addUser(User user) {
        if (repository.existsByUsername(user.getUsername())) throw new UsernameInUseException(UserMsg.USERNAME_IN_USE);
        return repository.saveClient(user);
    }

    public User setActive(UUID id, boolean active) {
        if(getUser(id) == null) throw new UserNotFoundException(UserMsg.USER_NOT_FOUND);
        return repository.setActive(id, active);
    }

    public User updateUser(UUID id, User user) {
        if(getUser(id) == null) throw new UserNotFoundException(UserMsg.USER_NOT_FOUND);
        return repository.updateUser(id, user);
    }
}
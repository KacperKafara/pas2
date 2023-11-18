package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.exceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.UsernameInUseException;
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
        return repository.findUser(id);
    }

    public User getUser(String username) {
        return repository.findUser(username);
    }

    public List<User> getUsers() {
        return repository.findUsers();
    }

    public List<User> getUsersByPattern(String pattern) {
        return repository.findUsersMatchToValue(pattern);
    }

    public User addUser(User user) {
        if(getUser(user.getUsername()) != null) throw new UsernameInUseException(UserMsg.USERNAME_IN_USE);
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
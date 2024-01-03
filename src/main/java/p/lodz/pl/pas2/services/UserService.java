package p.lodz.pl.pas2.services;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoWriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.exceptions.userExceptions.ThereIsNoUserToUpdateException;
import p.lodz.pl.pas2.exceptions.userExceptions.UserNotFoundException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsernameInUseException;
import p.lodz.pl.pas2.exceptions.userExceptions.UsersNotFoundException;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.msg.UserMsg;
import p.lodz.pl.pas2.repositories.UserRepository;
import p.lodz.pl.pas2.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Scope("prototype")
public class UserService {

    private final UserRepository repository;
    @Autowired
    public UserService(@Qualifier("userRepositoryMongoDB") UserRepository repository) {
        this.repository = repository;
    }

    public User getUser(UUID id) {
        User user = repository.findUser(id);
        if (user == null) throw new UserNotFoundException(UserMsg.USER_NOT_FOUND);
        return user;
    }

    public User getUser(String username) {
        User user = repository.findUser(username);
        if (user == null) throw new UserNotFoundException(UserMsg.USER_NOT_FOUND);
        return user;
    }

    public List<User> getUsers() {
        List<User> users = repository.findUsers();
        if(users.isEmpty()) throw new UsersNotFoundException(UserMsg.USERS_NOT_FOUND);
        return users;
    }

    public List<User> getUsersByPattern(String pattern) {
        List<User> users = repository.findUsersMatchToValue(pattern);
        if(users.isEmpty()) throw new UsersNotFoundException(UserMsg.USERS_NOT_FOUND);
        return users;
    }

    public User addUser(User user) {
        User added;
        try {
            added = repository.saveClient(user);
        } catch (MongoWriteException e){
            throw new UsernameInUseException(UserMsg.USERNAME_IN_USE);
        }
        return added;
    }

    public User setActive(UUID id, boolean active) {
        if(repository.findUser(id) == null) throw new ThereIsNoUserToUpdateException(UserMsg.USER_NOT_FOUND);
        return repository.setActive(id, active);
    }

    public User updateUser(UUID id, User user) {
        if(repository.findUser(id) == null) throw new ThereIsNoUserToUpdateException(UserMsg.USER_NOT_FOUND);
        return repository.updateUser(id, user);
    }

    public List<User> getClients() {
        List<User> users = repository.findClients();
        if(users.isEmpty()) throw new UsersNotFoundException(UserMsg.USERS_NOT_FOUND);
        return users;
    }

    public User updateClient(UUID id, Client client) {
        if(repository.findUser(id) == null) throw new ThereIsNoUserToUpdateException(UserMsg.USER_NOT_FOUND);
        User user;
        try {
            user = repository.updateClient(id, client);
        } catch (MongoCommandException e){
            throw new UsernameInUseException(UserMsg.USERNAME_IN_USE);
        }
        return user;
    }
}
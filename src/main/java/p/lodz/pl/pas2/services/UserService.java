package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.repositories.Implementations.UserRepositoryImplementation;
import p.lodz.pl.pas2.repositories.Implementations.mongoDB.UserRepositoryMongoDB;
import p.lodz.pl.pas2.repositories.UserRepository;
import p.lodz.pl.pas2.model.User;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
//    private final UserRepositoryMongoDB repository;
//    @Autowired
//    public UserService(UserRepositoryMongoDB repository) {
//        this.repository = repository;
//    }

    private final UserRepositoryImplementation repository;
    @Autowired
    public UserService(UserRepositoryImplementation repository) {
        this.repository = repository;
    }

    public User getUser(UUID id) {
        return repository.findUser(id);
    }

    public User getUser(String username) {
        return repository.findUser(username);
    }

    public UUID getUserID(User user) {
        return repository.findUser(user.getId()).getId();
    }

    public List<User> getUsers() {
        return repository.findUsers();
    }

    public User addClient(User user) {
        if(user.getUserName().isEmpty()) return null;
        if(getUser(user.getUserName()) != null) return null;
        return repository.saveClient(user);
    }

    public User setActive(UUID id, boolean active) {
        return repository.setActive(id, active);
    }

    public User updateClient(UUID id, User user) {
        if(user.getUserName().isEmpty()) return null;
        if(getUser(user.getUserName()) != null) return null;
        return repository.updateUser(id, user);
    }
}

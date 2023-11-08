package p.lodz.pl.pas2.repositories.Implementations;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.repositories.AbstractMongoRepository;
import p.lodz.pl.pas2.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryImplementation implements UserRepository {

    private final List<User> users;
    private final AbstractMongoRepository repository;
    public UserRepositoryImplementation(AbstractMongoRepository repository) {
        this.repository = repository;
        users = new ArrayList<>();

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
        user.setId(UUID.randomUUID());
        users.add(user);
        return user;
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
        updatedUser.setUserType(user.getUserType());
        updatedUser.setActive(user.isActive());
        return updatedUser;
    }
}

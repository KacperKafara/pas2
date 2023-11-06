package p.lodz.pl.pas2.repositories.Implementations;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.User;
import p.lodz.pl.pas2.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ClientRepositoryImplementation implements ClientRepository {

    private final List<User> users;
    public ClientRepositoryImplementation() {
        users = new ArrayList<>();
    }

    @Override
    public User findClient(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findClient(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findClientsMatchToValue(String username) {
        return null;
    }

    @Override
    public User saveClient(User user) {
        user.setId(UUID.randomUUID());
        users.add(user);
        return user;
    }

    @Override
    public List<User> findClients() {
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
    public User updateClient(UUID id, User user) {
        User updatedUser = findClient(id);
        updatedUser.setUsername(user.getUsername());
        updatedUser.setUserType(user.getUserType());
        updatedUser.setActive(user.isActive());
        return updatedUser;
    }
}

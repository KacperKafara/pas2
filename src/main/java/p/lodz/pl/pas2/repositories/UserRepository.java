package p.lodz.pl.pas2.repositories;

import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.model.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User findUser(UUID id);
    User findUser(String username);
    List<User> findUsersMatchToValue(String username);
    User saveClient(User user);
    List<User> findUsers();
    User setActive(UUID id, boolean active);
    User updateUser(UUID id, User user);
    List<User> findClients();
}

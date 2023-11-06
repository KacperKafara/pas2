package p.lodz.pl.pas2.repositories;

import p.lodz.pl.pas2.model.User;

import java.util.List;
import java.util.UUID;

public interface ClientRepository {
    User findClient(UUID id);
    User findClient(String username);
    List<User> findClientsMatchToValue(String username);
    User saveClient(User user);
    List<User> findClients();
    User setActive(UUID id, boolean active);
    User updateClient(UUID id, User user);
}

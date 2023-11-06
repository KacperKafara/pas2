package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.repositories.ClientRepository;
import p.lodz.pl.pas2.model.User;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository repository;
    @Autowired
    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public User getClient(UUID id) {
        return repository.findClient(id);
    }

    public User getClient(String username) {
        return repository.findClient(username);
    }

    public List<User> getClients() {
        return repository.findClients();
    }

    public User addClient(User user) {
        if(user.getUsername().isEmpty()) return null;
        if(getClient(user.getUsername()) != null) return null;
        return repository.saveClient(user);
    }

    public User setActive(UUID id, boolean active) {
        return repository.setActive(id, active);
    }

    public User updateClient(UUID id, User user) {
        if(user.getUsername().isEmpty()) return null;
        if(getClient(user.getUsername()) != null) return null;
        return repository.updateClient(id, user);
    }
}

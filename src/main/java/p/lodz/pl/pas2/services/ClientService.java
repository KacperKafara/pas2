package p.lodz.pl.pas2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.lodz.pl.pas2.repositories.ClientRepository;
import p.lodz.pl.pas2.model.Client;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository repository;
    @Autowired
    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Client getClient(UUID id) {
        return repository.findClient(id);
    }

    public Client getClient(String username) {
        return repository.findClient(username);
    }

    public List<Client> getClients() {
        return repository.findClients();
    }

    public Client addClient(Client client) {
        if(client.getUsername().isEmpty()) return null;
        if(getClient(client.getUsername()) != null) return null;
        return repository.saveClient(client);
    }

    public Client setActive(UUID id, boolean active) {
        return repository.setActive(id, active);
    }

    public Client updateClient(UUID id, Client client) {
        if(client.getUsername().isEmpty()) return null;
        if(getClient(client.getUsername()) != null) return null;
        return repository.updateClient(id, client);
    }
}

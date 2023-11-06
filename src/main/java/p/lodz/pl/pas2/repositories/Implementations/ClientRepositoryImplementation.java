package p.lodz.pl.pas2.repositories.Implementations;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Client;
import p.lodz.pl.pas2.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ClientRepositoryImplementation implements ClientRepository {

    private final List<Client> clients;
    public ClientRepositoryImplementation() {
        clients = new ArrayList<>();
    }

    @Override
    public Client findClient(UUID id) {
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public Client findClient(String username) {
        for (Client client : clients) {
            if (client.getUsername().equals(username)) {
                return client;
            }
        }
        return null;
    }

    @Override
    public List<Client> findClientsMatchToValue(String username) {
        return null;
    }

    @Override
    public Client saveClient(Client client) {
        client.setId(UUID.randomUUID());
        clients.add(client);
        return client;
    }

    @Override
    public List<Client> findClients() {
        return clients;
    }

    @Override
    public Client setActive(UUID id, boolean active) {
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                client.setActive(active);
                return client;
            }
        }
        return null;
    }

    @Override
    public Client changeUsername(UUID id, String username) {
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                client.setUsername(username);
                return client;
            }
        }
        return null;
    }
}

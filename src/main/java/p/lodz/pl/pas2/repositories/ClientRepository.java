package p.lodz.pl.pas2.repositories;

import org.springframework.stereotype.Repository;
import p.lodz.pl.pas2.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientRepository {
    Client findClient(UUID id);
    Client findClient(String username);
    List<Client> findClientsMatchToValue(String username);
    Client saveClient(Client client);
    List<Client> findClients();
    Client setActive(UUID id, boolean active);
    Client updateClient(UUID id, Client client);
}
